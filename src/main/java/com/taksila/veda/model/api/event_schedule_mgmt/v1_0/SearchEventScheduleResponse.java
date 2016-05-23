//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.09 at 08:22:33 AM EDT 
//


package com.taksila.veda.model.api.event_schedule_mgmt.v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.taksila.veda.model.api.base.v1_0.BaseResponse;
import com.taksila.veda.model.db.event_schedule_mgmt.v1_0.EventSchedule;


/**
 * 
 * 				This represents the api structure of the SearchEventScheduleResponse response 				
 * 			
 * 
 * <p>Java class for SearchEventScheduleResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SearchEventScheduleResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.taksila.com/veda/model/api/base/v1_0}BaseResponse">
 *       &lt;sequence>
 *         &lt;element name="eventSchedule" type="{http://www.taksila.com/veda/model/db/event_schedule_mgmt/v1_0}EventSchedule" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SearchEventScheduleResponse", propOrder = {
    "eventSchedule"
})
public class SearchEventScheduleResponse
    extends BaseResponse
{

    protected List<EventSchedule> eventSchedule;

    /**
     * Gets the value of the eventSchedule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventSchedule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEventSchedule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventSchedule }
     * 
     * 
     */
    public List<EventSchedule> getEventSchedule() {
        if (eventSchedule == null) {
            eventSchedule = new ArrayList<EventSchedule>();
        }
        return this.eventSchedule;
    }

}