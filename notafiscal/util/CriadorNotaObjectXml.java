/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.datagrupo.buyshowloja.webservices.notafiscal.util;

import br.com.datagrupo.buyshowloja.model.dto.ContaCorrenteDTO;
import br.com.datagrupo.buyshowloja.model.vo.FornecedorVO;
import br.com.datagrupo.buyshowloja.model.vo.PaymentVO;
import br.com.datagrupo.buyshowloja.service.PaymentService;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcCpfCnpj;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcDadosServico;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcDadosTomador;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcEndereco;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcIdentificacaoPrestador;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcIdentificacaoRps;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcIdentificacaoTomador;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcInfRps;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcRps;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse.TcValores;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse_pcrj_v01.GerarNfseEnvio;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse_pcrj_v01.GerarNfseResposta;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.soap.wsImportClasses.GerarNfseResponse;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.soap.wsImportClasses.NotaServiceWs;
import br.com.datagrupo.core.util.Scopes;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author dg
 */
@Component
@Scope(Scopes.PROTOTYPE)
public class CriadorNotaObjectXml implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CriadorNotaObjectXml.class);

    private PaymentService paymentService;

    private ContaCorrenteDTO conta;

    private PaymentVO paymentVO;

    public ContaCorrenteDTO getConta() {
        return conta;
    }

    public void setConta(ContaCorrenteDTO conta) {
        this.conta = conta;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public PaymentVO getPaymentVO() {
        return paymentVO;
    }

    public void setPaymentVO(PaymentVO paymentVO) {
        this.paymentVO = paymentVO;
    }
    
    @Override
    public void run() {
        try {
            GerarNfseEnvio nfseEnvio = gerarNfseEnvio(this.conta);
            NotaServiceWs ns = new NotaServiceWs();
            GerarNfseResponse response = ns.gerarNota(nfseEnvio);
            GerarNfseResposta reposta = ns.criarResponse(response);
            String urlNota = gerarUrlNota(reposta);
            paymentVO.setUrlNota(urlNota);
            paymentService.saveOrUpdate(paymentVO, null);

        } catch (JAXBException ex) {
            LOGGER.error("Erro ao gerar nota fiscal", ex);
        }
    }

    public String gerarUrlNota(GerarNfseResposta resposta) {
        String inscricaoMunicipal = "6442528";
        String numeroNf = resposta.getCompNfse().getNfse().getInfNfse().getNumero().toString();
        String codigoVerificacaoNf = resposta.getCompNfse().getNfse().getInfNfse().getCodigoVerificacao().replace("-", "");
        return new StringBuilder().
                append("https://notacarioca.rio.gov.br/nfse.aspx?").
                append("inscricao=").append(inscricaoMunicipal).
                append("&nf=").append(numeroNf).
                append("&cod=").append(codigoVerificacaoNf).
                toString();

    }

    public GerarNfseEnvio gerarNfseEnvio(ContaCorrenteDTO conta) {
        GerarNfseEnvio nfseEnvio = new GerarNfseEnvio();
        nfseEnvio.setRps(montarTcRps(conta));
        return nfseEnvio;
    }

    public TcRps montarTcRps(ContaCorrenteDTO conta) {
        TcRps tcRps = new TcRps();
        tcRps.setInfRps(montarTcInfRps(conta));
        return tcRps;
    }

    public TcInfRps montarTcInfRps(ContaCorrenteDTO conta) {
        TcInfRps infRps = new TcInfRps();
        infRps.setIdentificacaoRps(montarTcIdentificacaoRps());
        infRps.setDataEmissao(gerarDataFormatada());
        infRps.setNaturezaOperacao(Byte.valueOf("1")); // tributação município
        infRps.setOptanteSimplesNacional(Byte.valueOf("1")); // sim
        infRps.setIncentivadorCultural(Byte.valueOf("2")); // nao
        infRps.setStatus(Byte.valueOf("1"));
        infRps.setServico(montarTcDadosServico(conta));
        infRps.setPrestador(montarTcIdentificacaoPrestador());
        infRps.setTomador(montarTcDadosTomador(conta.getFornecedor()));
        return infRps;
    }

    //dados do cliente
    public TcDadosTomador montarTcDadosTomador(FornecedorVO fornecedor) {
        TcDadosTomador tomador = new TcDadosTomador();
        tomador.setIdentificacaoTomador(montarTcIdentificacaoTomador(fornecedor));
        tomador.setEndereco(montarTcEndereco(fornecedor));
        tomador.setRazaoSocial(fornecedor.getRazaoSocial());
        return tomador;
    }

    //dados do cliente
    public TcIdentificacaoTomador montarTcIdentificacaoTomador(FornecedorVO fornecedor) {
        TcIdentificacaoTomador identificacaoTomador = new TcIdentificacaoTomador();
        TcCpfCnpj tomadorCnpj = new TcCpfCnpj();
        tomadorCnpj.setCnpj(fornecedor.getCnpj().replace(".", "").replace("/", "").replace("-", ""));
        identificacaoTomador.setCpfCnpj(tomadorCnpj);
        return identificacaoTomador;
    }

    //dados do cliente
    public TcEndereco montarTcEndereco(FornecedorVO fornecedor) {
        TcEndereco enderecoTomador = new TcEndereco();
        enderecoTomador.setEndereco(fornecedor.getLogradouro());
        enderecoTomador.setNumero(fornecedor.getNumero());
        enderecoTomador.setComplemento(fornecedor.getComplemento());
        enderecoTomador.setBairro(fornecedor.getBairro());
        enderecoTomador.setCodigoMunicipio(3304557); // codigo do rio de janeiro
        enderecoTomador.setUf(fornecedor.getEstado().getUf());
        enderecoTomador.setCep(Integer.parseInt(fornecedor.getCep()));
        return enderecoTomador;
    }

    //dados da nota fiscal
    public TcIdentificacaoRps montarTcIdentificacaoRps() {
        TcIdentificacaoRps identificacaoRps = new TcIdentificacaoRps();
        identificacaoRps.setNumero(buscarNumeroInterno());// gerar pelo sistema usando o sequence do banco
        identificacaoRps.setSerie("1");
        identificacaoRps.setTipo(Byte.valueOf("1")); // rps tipo nota fiscal
        return identificacaoRps;
    }

    public BigInteger buscarNumeroInterno() {
        return paymentService.getNumSequence();
    }

    //dados do serviço
    public TcDadosServico montarTcDadosServico(ContaCorrenteDTO contaSelecionada) {
        TcDadosServico servico = new TcDadosServico();
        servico.setValores(montarTcValores(contaSelecionada.getValor()));
        servico.setItemListaServico("0105"); //Código do serviço prestado. Item da LC 116/2003 
        servico.setCodigoTributacaoMunicipio("010501");//Código do serviço prestado próprio do município 
        servico.setDiscriminacao("Plano mensal do Buyshow portal");//Discriminação dos serviços em até 2000 caracteres
        servico.setCodigoMunicipio(3304557); // codigo do rio de janeiro
        return servico;
    }

    //valores e impostos
    public TcValores montarTcValores(BigDecimal valor) {
        TcValores valoresServico = new TcValores();
        valoresServico.setValorServicos(valor);
        valoresServico.setValorDeducoes(BigDecimal.ZERO);
        valoresServico.setValorPis(BigDecimal.ZERO);
        valoresServico.setValorCofins(BigDecimal.ZERO);
        valoresServico.setValorInss(BigDecimal.ZERO);
        valoresServico.setValorIr(BigDecimal.ZERO);
        valoresServico.setValorCsll(BigDecimal.ZERO);
        valoresServico.setIssRetido(Byte.valueOf("2"));
        valoresServico.setValorIss(BigDecimal.ZERO);
        valoresServico.setOutrasRetencoes(BigDecimal.ZERO);
        valoresServico.setAliquota(BigDecimal.valueOf(0.06D)); //Condicionada ao faturamento bruto anual Ex.: faturamento anual de até 180.000,00 alíquota inicial de 6%.
        valoresServico.setDescontoIncondicionado(BigDecimal.ZERO);
        valoresServico.setDescontoCondicionado(BigDecimal.ZERO);
        return valoresServico;
    }

    //dados do buyshow
    public TcIdentificacaoPrestador montarTcIdentificacaoPrestador() {
        TcIdentificacaoPrestador prestador = new TcIdentificacaoPrestador();
        prestador.setCnpj("22595968000140"); //buyshow
        prestador.setInscricaoMunicipal("06442528"); //buyshow
        return prestador;
    }

    public XMLGregorianCalendarImpl gerarDataFormatada() {
        try {
            Instant now = Instant.now();
            String dateTimeString = now.toString();
            XMLGregorianCalendar dataAtual = DatatypeFactory.
                    newInstance().newXMLGregorianCalendar(dateTimeString);
            return new XMLGregorianCalendarImpl(dataAtual.toGregorianCalendar());
        } catch (DatatypeConfigurationException ex) {
            LOGGER.error("Erro ao formatar a data", ex);
            return null;
        }
    }

    /*
    
FORNECIDO PELO CONTADOR DO ALEXANDRE
    
Sobre a identificação da nota fiscal:
Qual numero: Gerado pelo programa para indicar a ordem de emissão ex.: (nota 1,2,3,4, etc.)
Qual Serie: 1
Qual Tipo: 0 Entrada / 1 Saída
Qual Status: Gerado após a emissão (emitida/cancelada/recusada).

Sobre o serviço:
Código do serviço prestado, Item da LC 116/2003: Itens 1 e 10 
Codigo de tributação do município: 01.05.01
Codigo do município: não exigido pela NFEe.

Sobre os valores:
Quais impostos são pagos: PIS, COFINS, CSLL, IRPF, ISS ou ICMS E INSS de forma unificada apuradas dentro do simples nacional.
Valor de deduções: Sem deduções
VAlor Pis: 0,00
Valor Cofins: 0,00
Valor Inss: 0,00
Valor Csll: 0,00
tipo Iss retido: 0,00
Valor Iss : 0,00
Outras Retenções: 0,00
Aliquota: Condicionada ao faturamento bruto anual Ex.: faturamento anual de até 180.000,00 alíquota inicial de 6%.
Descondo Condicionado: 0,00
Desconto Incondicionado: 0,00
    
     */
}
