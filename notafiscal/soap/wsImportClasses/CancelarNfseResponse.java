
package br.com.datagrupo.buyshowloja.webservices.notafiscal.soap.wsImportClasses;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="outputXML" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "outputXML"
})
@XmlRootElement(name = "CancelarNfseResponse")
public class CancelarNfseResponse {

    protected String outputXML;

    /**
     * Gets the value of the outputXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutputXML() {
        return outputXML;
    }

    /**
     * Sets the value of the outputXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutputXML(String value) {
        this.outputXML = value;
    }

}
