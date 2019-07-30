//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.28 at 03:07:13 PM BRT 
//


package br.com.datagrupo.buyshowloja.webservices.notafiscal.models.nfse;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tcIdentificacaoNfse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tcIdentificacaoNfse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Numero" type="{http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd}tsNumeroNfse"/>
 *         &lt;element name="Cnpj" type="{http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd}tsCnpj"/>
 *         &lt;element name="InscricaoMunicipal" type="{http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd}tsInscricaoMunicipal" minOccurs="0"/>
 *         &lt;element name="CodigoMunicipio" type="{http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd}tsCodigoMunicipioIbge"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tcIdentificacaoNfse", propOrder = {
    "numero",
    "cnpj",
    "inscricaoMunicipal",
    "codigoMunicipio"
})
public class TcIdentificacaoNfse {

    @XmlElement(name = "Numero", required = true)
    protected BigInteger numero;
    @XmlElement(name = "Cnpj", required = true)
    protected String cnpj;
    @XmlElement(name = "InscricaoMunicipal")
    protected String inscricaoMunicipal;
    @XmlElement(name = "CodigoMunicipio")
    protected int codigoMunicipio;

    /**
     * Gets the value of the numero property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumero() {
        return numero;
    }

    /**
     * Sets the value of the numero property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumero(BigInteger value) {
        this.numero = value;
    }

    /**
     * Gets the value of the cnpj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * Sets the value of the cnpj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCnpj(String value) {
        this.cnpj = value;
    }

    /**
     * Gets the value of the inscricaoMunicipal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    /**
     * Sets the value of the inscricaoMunicipal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInscricaoMunicipal(String value) {
        this.inscricaoMunicipal = value;
    }

    /**
     * Gets the value of the codigoMunicipio property.
     * 
     */
    public int getCodigoMunicipio() {
        return codigoMunicipio;
    }

    /**
     * Sets the value of the codigoMunicipio property.
     * 
     */
    public void setCodigoMunicipio(int value) {
        this.codigoMunicipio = value;
    }

}
