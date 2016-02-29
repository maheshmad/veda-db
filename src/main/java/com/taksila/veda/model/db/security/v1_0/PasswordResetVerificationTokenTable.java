//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.29 at 03:53:26 PM EST 
//


package com.taksila.veda.model.db.security.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.taksila.veda.model.api.security.v1_0.PasswordResetVerificationToken;


/**
 * 
 * 				This represents the structure of the password verification token table				
 * 			
 * 
 * <p>Java class for PasswordResetVerificationTokenTable complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PasswordResetVerificationTokenTable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/security/v1_0}token"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/security/v1_0}username"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/security/v1_0}tokenExpiryDateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PasswordResetVerificationTokenTable", propOrder = {
    "token",
    "username",
    "tokenExpiryDateTime"
})
@XmlSeeAlso({
    PasswordResetVerificationToken.class
})
public class PasswordResetVerificationTokenTable {

    @XmlElement(required = true)
    protected String token;
    @XmlElement(required = true)
    protected String username;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar tokenExpiryDateTime;

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the tokenExpiryDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTokenExpiryDateTime() {
        return tokenExpiryDateTime;
    }

    /**
     * Sets the value of the tokenExpiryDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTokenExpiryDateTime(XMLGregorianCalendar value) {
        this.tokenExpiryDateTime = value;
    }

}