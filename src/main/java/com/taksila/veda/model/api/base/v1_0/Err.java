//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.29 at 03:53:26 PM EST 
//


package com.taksila.veda.model.api.base.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Err complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Err">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.taksila.com/veda/model/api/base/v1_0}errorFieldId"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/api/base/v1_0}errorMsg"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Err", propOrder = {
    "errorFieldId",
    "errorMsg"
})
public class Err {

    @XmlElement(required = true)
    protected String errorFieldId;
    @XmlElement(required = true)
    protected String errorMsg;

    /**
     * Gets the value of the errorFieldId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorFieldId() {
        return errorFieldId;
    }

    /**
     * Sets the value of the errorFieldId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorFieldId(String value) {
        this.errorFieldId = value;
    }

    /**
     * Gets the value of the errorMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Sets the value of the errorMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMsg(String value) {
        this.errorMsg = value;
    }

}