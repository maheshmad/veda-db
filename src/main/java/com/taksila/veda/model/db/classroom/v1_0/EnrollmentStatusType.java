//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.21 at 07:43:54 AM EDT 
//


package com.taksila.veda.model.db.classroom.v1_0;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EnrollmentStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="EnrollmentStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="SUSPENDED"/>
 *     &lt;enumeration value="CLOSED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "EnrollmentStatusType")
@XmlEnum
public enum EnrollmentStatusType {

    ACTIVE,
    SUSPENDED,
    CLOSED;

    public String value() {
        return name();
    }

    public static EnrollmentStatusType fromValue(String v) {
        return valueOf(v);
    }

}