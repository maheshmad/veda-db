//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.03 at 10:48:05 PM EDT 
//


package com.taksila.veda.model.api.event_schedule_mgmt.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventSchedule;


/**
 * 
 * 				This represents the api structure of the UpdateEventScheduleResponse response 				
 * 			
 * 
 * <p>Java class for UpdateEventScheduleResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpdateEventScheduleResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.taksila.com/veda/model/api/base/v1_0}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="eventSchedule" type="{http://www.taksila.com/veda/model/db/event_schedule_mgmt/v1_0}EventSchedule"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateEventScheduleResponse", propOrder = {
    "eventSchedule"
})
public class UpdateEventScheduleResponse
    extends BaseResponse
{

    @XmlElement(required = true)
    protected EventSchedule eventSchedule;

    /**
     * Gets the value of the eventSchedule property.
     * 
     * @return
     *     possible object is
     *     {@link EventSchedule }
     *     
     */
    public EventSchedule getEventSchedule() {
        return eventSchedule;
    }

    /**
     * Sets the value of the eventSchedule property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventSchedule }
     *     
     */
    public void setEventSchedule(EventSchedule value) {
        this.eventSchedule = value;
    }

}
