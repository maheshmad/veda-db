//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.12 at 06:19:21 AM EDT 
//


package com.taksila.veda.model.db.base.v1_0;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserRole.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UserRole">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SYSADMIN"/>
 *     &lt;enumeration value="ADMIN"/>
 *     &lt;enumeration value="STUDENT"/>
 *     &lt;enumeration value="TEACHER"/>
 *     &lt;enumeration value="PRINCIPAL"/>
 *     &lt;enumeration value="PARENT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UserRole")
@XmlEnum
public enum UserRole {

    SYSADMIN,
    ADMIN,
    STUDENT,
    TEACHER,
    PRINCIPAL,
    PARENT;

    public String value() {
        return name();
    }

    public static UserRole fromValue(String v) {
        return valueOf(v);
    }

}
