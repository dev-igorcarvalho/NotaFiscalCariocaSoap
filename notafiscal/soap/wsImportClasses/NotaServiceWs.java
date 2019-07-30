/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.datagrupo.buyshowloja.webservices.notafiscal.soap.wsImportClasses;

import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse_pcrj_v01.GerarNfseEnvio;
import br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse_pcrj_v01.GerarNfseResposta;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author dg
 */
public class NotaServiceWs {

    private final NfseSoap proxy;

    public NotaServiceWs() {
        proxy = new Nfse().getNfseSoap();
    }

    private void autentica() {

        String caminhoDoCertificadoDoCliente = "/home/dg/Documents/Projetos Datagrupo/buyshow-loja/buyshow-loja-core/src/main/java/br/com/datagrupo/buyshowloja/webservices/notafiscal/soap/cerfificadoDigital/certificado-buyshow.pfx";
        String senhaDoCertificadoDoCliente = "multiplaconsultoria123";
        String caminhoDoKeyStore = "/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/security/cacerts";
        String senhaDoKeyStore = "changeit";

        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");

        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        System.setProperty("javax.net.ssl.keyStore", caminhoDoCertificadoDoCliente);
        System.setProperty("javax.net.ssl.keyStorePassword", senhaDoCertificadoDoCliente);

        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("javax.net.ssl.trustStore", caminhoDoKeyStore);
        System.setProperty("javax.net.ssl.trustStorePassword", senhaDoKeyStore);

    }

    public GerarNfseResponse gerarNota(GerarNfseEnvio notaPreenchida) {
        try {
            this.autentica();
            GerarNfseRequest request = criarRequest(notaPreenchida);
            GerarNfseResponse response = proxy.gerarNfse(request);
            System.out.println(response.outputXML);
            return response;
        } catch (JAXBException ex) {
            Logger.getLogger(NotaServiceWs.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

//    colocar pra receber um DTO dos dadnos necessarios
    private GerarNfseRequest criarRequest(GerarNfseEnvio notaPreenchida) throws JAXBException {

        GerarNfseRequest request = new ObjectFactory().createGerarNfseRequest();

        JAXBContext jc = JAXBContext.newInstance(GerarNfseEnvio.class);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

        StringWriter sw = new StringWriter();
        m.marshal(notaPreenchida, sw);

//      colocar o xml gerado no request
        request.setInputXML(sw.toString());
//        request.setInputXML(marreta());

        return request;
    }

    public GerarNfseResposta criarResponse(GerarNfseResponse response) throws JAXBException {

        JAXBContext jc = JAXBContext.newInstance(GerarNfseResposta.class);
        Unmarshaller u = jc.createUnmarshaller();

        StringReader sr = new StringReader(response.outputXML);
        GerarNfseResposta resposta = (GerarNfseResposta) u.unmarshal(sr);
        return resposta;
    }

    public GerarNfseResposta criarRespostaMArreta() throws JAXBException {
        this.autentica();
        JAXBContext jc = JAXBContext.newInstance(GerarNfseResposta.class);
        Unmarshaller u = jc.createUnmarshaller();

        StringReader sr = new StringReader(marreta());
        GerarNfseResposta resposta = (GerarNfseResposta) u.unmarshal(sr);
        return resposta;
    }

    public String marreta() {
        String marretaEmLinhaSemSignature = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><GerarNfseEnvio xmlns=\"http://notacarioca.rio.gov.br/WSNacional/XSD/1/nfse_pcrj_v01.xsd\"><Rps><InfRps xmlns=\"http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd\" Id=\"R1\"><IdentificacaoRps><Numero>1</Numero><Serie>ABC</Serie><Tipo>1</Tipo></IdentificacaoRps><DataEmissao>2010-01-01T21:00:00</DataEmissao><NaturezaOperacao>1</NaturezaOperacao><OptanteSimplesNacional>2</OptanteSimplesNacional><IncentivadorCultural>2</IncentivadorCultural><Status>1</Status><Servico><Valores><ValorServicos>1000.00</ValorServicos><ValorDeducoes>0</ValorDeducoes><ValorPis>10.00</ValorPis><ValorCofins>10.00</ValorCofins><ValorInss>10.00</ValorInss><ValorIr>10.00</ValorIr><ValorCsll>10.00</ValorCsll><IssRetido>2</IssRetido><ValorIss>10.00</ValorIss><OutrasRetencoes>10.00</OutrasRetencoes><Aliquota>0.05</Aliquota><DescontoIncondicionado>10.00</DescontoIncondicionado><DescontoCondicionado>10.00</DescontoCondicionado></Valores><ItemListaServico>0102</ItemListaServico><CodigoTributacaoMunicipio>010201</CodigoTributacaoMunicipio><Discriminacao>Teste</Discriminacao><CodigoMunicipio>3304557</CodigoMunicipio></Servico><Prestador><Cnpj>04642554000143</Cnpj><InscricaoMunicipal>2994275</InscricaoMunicipal></Prestador><Tomador><IdentificacaoTomador><CpfCnpj><Cnpj>99999999000191</Cnpj></CpfCnpj></IdentificacaoTomador><RazaoSocial>INSCRICAO DE TESTE</RazaoSocial><Endereco><Endereco>AV RIO BRANCO</Endereco><Numero>12345</Numero><Complemento>SALA 1001 1002</Complemento><Bairro>CENTRO</Bairro><CodigoMunicipio>3304557</CodigoMunicipio><Uf>RJ</Uf><Cep>20040001</Cep></Endereco></Tomador></InfRps></Rps></GerarNfseEnvio>";
        String marretaResposta = "<GerarNfseResposta xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://notacarioca.rio.gov.br/WSNacional/XSD/1/nfse_pcrj_v01.xsd\"><CompNfse xmlns=\"http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd\"><Nfse><InfNfse><Numero>24</Numero><CodigoVerificacao>TIRB-UXAJ</CodigoVerificacao><DataEmissao>2019-07-16T16:06:01</DataEmissao><IdentificacaoRps><Numero>1</Numero><Serie>1</Serie><Tipo>1</Tipo></IdentificacaoRps><DataEmissaoRps>2019-07-16</DataEmissaoRps><NaturezaOperacao>1</NaturezaOperacao><OptanteSimplesNacional>1</OptanteSimplesNacional><IncentivadorCultural>2</IncentivadorCultural><Competencia>2019-07-16T00:00:00Z</Competencia><Servico><Valores><ValorServicos>10</ValorServicos><IssRetido>2</IssRetido><BaseCalculo>10</BaseCalculo><ValorLiquidoNfse>10</ValorLiquidoNfse></Valores><ItemListaServico>0105</ItemListaServico><CodigoTributacaoMunicipio>010501</CodigoTributacaoMunicipio><Discriminacao>Plano mensal do Buyshow portal</Discriminacao><CodigoMunicipio>3304557</CodigoMunicipio></Servico><PrestadorServico><IdentificacaoPrestador><Cnpj>22595968000140</Cnpj><InscricaoMunicipal>6442528</InscricaoMunicipal></IdentificacaoPrestador><RazaoSocial>B2BID SERVICOS E NEGOCIOS LTDA</RazaoSocial><NomeFantasia>B2BUS NEGOCIOS INTELIGENTES</NomeFantasia><Endereco><Endereco>AVN MAL FLORIANO 38, APT 301</Endereco><Numero>38</Numero><Complemento>APT 301</Complemento><Bairro>CENTRO</Bairro><CodigoMunicipio>3304557</CodigoMunicipio><Uf>RJ</Uf><Cep>20080007</Cep></Endereco><Contato><Telefone>22216249</Telefone><Email>alexandre.reis@b2bus.com.br</Email></Contato></PrestadorServico><TomadorServico><IdentificacaoTomador><CpfCnpj><Cnpj>16043040000150</Cnpj></CpfCnpj></IdentificacaoTomador><RazaoSocial>16.043.040/ 0001-50</RazaoSocial><Endereco><Endereco>Rua Américo Rocha 123, 123</Endereco><Numero>123</Numero><Complemento>123</Complemento><Bairro>Marechal Hermes</Bairro><CodigoMunicipio>3304557</CodigoMunicipio><Uf>RJ</Uf><Cep>21555300</Cep></Endereco></TomadorServico><OrgaoGerador><CodigoMunicipio>3304557</CodigoMunicipio><Uf>RJ</Uf></OrgaoGerador></InfNfse></Nfse></CompNfse></GerarNfseResposta>";
        return marretaResposta;
    }
}
