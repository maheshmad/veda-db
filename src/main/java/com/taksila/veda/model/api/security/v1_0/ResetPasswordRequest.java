//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.29 at 04:53:10 PM EST 
//


package com.taksila.veda.model.api.security.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.taksila.veda.model.api.base.v1_0.BaseRequest;


/**
 * 
 * 				This represents the api structure of the ResetPasswordRequest api request 				
 * 			
 * 
 * <p>Java class for ResetPasswordRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResetPasswordRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.taksila.com/veda/model/api/base/v1_0}BaseRequest">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/security/v1_0}token"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/db/security/v1_0}username"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/api/security/v1_0}newPassword"/>
 *         &lt;element ref="{http://www.taksila.com/veda/model/api/security/v1_0}newPasswordConfirm"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResetPasswordRequest", propOrder = {
    "token",
    "username",
    "newPassword",
    "newPasswordConfirm"
})
public class ResetPasswordRequest
    extends BaseRequest
{

    @XmlElement(namespace = "http://www.taksila.com/veda/model/db/security/v1_0", required = true)
    protected String token;
    @XmlElement(namespace = "http://www.taksila.com/veda/model/db/security/v1_0", required = true)
    protected String username;
    @XmlElement(required = true)
    protected String newPassword;
    @XmlElement(required = true)
    protected String newPasswordConfirm;

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
     * Gets the value of the newPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the value of the newPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewPassword(String value) {
        this.newPassword = value;
    }

    /**
     * Gets the value of the newPasswordConfirm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    /**
     * Sets the value of the newPasswordConfirm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewPasswordConfirm(String value) {
        this.newPasswordConfirm = value;
    }

}
