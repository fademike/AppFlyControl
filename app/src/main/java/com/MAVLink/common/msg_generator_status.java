/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE GENERATOR_STATUS PACKING
package com.MAVLink.common;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
import com.MAVLink.Messages.Units;
import com.MAVLink.Messages.Description;

/**
 * Telemetry of power generation system. Alternator or mechanical generator.
 */
public class msg_generator_status extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_GENERATOR_STATUS = 373;
    public static final int MAVLINK_MSG_LENGTH = 42;
    private static final long serialVersionUID = MAVLINK_MSG_ID_GENERATOR_STATUS;

    
    /**
     * Status flags.
     */
    @Description("Status flags.")
    @Units("")
    public long status;
    
    /**
     * Current into/out of battery. Positive for out. Negative for in. NaN: field not provided.
     */
    @Description("Current into/out of battery. Positive for out. Negative for in. NaN: field not provided.")
    @Units("A")
    public float battery_current;
    
    /**
     * Current going to the UAV. If battery current not available this is the DC current from the generator. Positive for out. Negative for in. NaN: field not provided
     */
    @Description("Current going to the UAV. If battery current not available this is the DC current from the generator. Positive for out. Negative for in. NaN: field not provided")
    @Units("A")
    public float load_current;
    
    /**
     * The power being generated. NaN: field not provided
     */
    @Description("The power being generated. NaN: field not provided")
    @Units("W")
    public float power_generated;
    
    /**
     * Voltage of the bus seen at the generator, or battery bus if battery bus is controlled by generator and at a different voltage to main bus.
     */
    @Description("Voltage of the bus seen at the generator, or battery bus if battery bus is controlled by generator and at a different voltage to main bus.")
    @Units("V")
    public float bus_voltage;
    
    /**
     * The target battery current. Positive for out. Negative for in. NaN: field not provided
     */
    @Description("The target battery current. Positive for out. Negative for in. NaN: field not provided")
    @Units("A")
    public float bat_current_setpoint;
    
    /**
     * Seconds this generator has run since it was rebooted. UINT32_MAX: field not provided.
     */
    @Description("Seconds this generator has run since it was rebooted. UINT32_MAX: field not provided.")
    @Units("s")
    public long runtime;
    
    /**
     * Seconds until this generator requires maintenance.  A negative value indicates maintenance is past-due. INT32_MAX: field not provided.
     */
    @Description("Seconds until this generator requires maintenance.  A negative value indicates maintenance is past-due. INT32_MAX: field not provided.")
    @Units("s")
    public int time_until_maintenance;
    
    /**
     * Speed of electrical generator or alternator. UINT16_MAX: field not provided.
     */
    @Description("Speed of electrical generator or alternator. UINT16_MAX: field not provided.")
    @Units("rpm")
    public int generator_speed;
    
    /**
     * The temperature of the rectifier or power converter. INT16_MAX: field not provided.
     */
    @Description("The temperature of the rectifier or power converter. INT16_MAX: field not provided.")
    @Units("degC")
    public short rectifier_temperature;
    
    /**
     * The temperature of the mechanical motor, fuel cell core or generator. INT16_MAX: field not provided.
     */
    @Description("The temperature of the mechanical motor, fuel cell core or generator. INT16_MAX: field not provided.")
    @Units("degC")
    public short generator_temperature;
    

    /**
     * Generates the payload for a mavlink message for a message of this type
     * @return
     */
    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH,isMavlink2);
        packet.sysid = sysid;
        packet.compid = compid;
        packet.msgid = MAVLINK_MSG_ID_GENERATOR_STATUS;

        packet.payload.putUnsignedLong(status);
        packet.payload.putFloat(battery_current);
        packet.payload.putFloat(load_current);
        packet.payload.putFloat(power_generated);
        packet.payload.putFloat(bus_voltage);
        packet.payload.putFloat(bat_current_setpoint);
        packet.payload.putUnsignedInt(runtime);
        packet.payload.putInt(time_until_maintenance);
        packet.payload.putUnsignedShort(generator_speed);
        packet.payload.putShort(rectifier_temperature);
        packet.payload.putShort(generator_temperature);
        
        if (isMavlink2) {
            
        }
        return packet;
    }

    /**
     * Decode a generator_status message into this class fields
     *
     * @param payload The message to decode
     */
    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();

        this.status = payload.getUnsignedLong();
        this.battery_current = payload.getFloat();
        this.load_current = payload.getFloat();
        this.power_generated = payload.getFloat();
        this.bus_voltage = payload.getFloat();
        this.bat_current_setpoint = payload.getFloat();
        this.runtime = payload.getUnsignedInt();
        this.time_until_maintenance = payload.getInt();
        this.generator_speed = payload.getUnsignedShort();
        this.rectifier_temperature = payload.getShort();
        this.generator_temperature = payload.getShort();
        
        if (isMavlink2) {
            
        }
    }

    /**
     * Constructor for a new message, just initializes the msgid
     */
    public msg_generator_status() {
        this.msgid = MAVLINK_MSG_ID_GENERATOR_STATUS;
    }

    /**
     * Constructor for a new message, initializes msgid and all payload variables
     */
    public msg_generator_status( long status, float battery_current, float load_current, float power_generated, float bus_voltage, float bat_current_setpoint, long runtime, int time_until_maintenance, int generator_speed, short rectifier_temperature, short generator_temperature) {
        this.msgid = MAVLINK_MSG_ID_GENERATOR_STATUS;

        this.status = status;
        this.battery_current = battery_current;
        this.load_current = load_current;
        this.power_generated = power_generated;
        this.bus_voltage = bus_voltage;
        this.bat_current_setpoint = bat_current_setpoint;
        this.runtime = runtime;
        this.time_until_maintenance = time_until_maintenance;
        this.generator_speed = generator_speed;
        this.rectifier_temperature = rectifier_temperature;
        this.generator_temperature = generator_temperature;
        
    }

    /**
     * Constructor for a new message, initializes everything
     */
    public msg_generator_status( long status, float battery_current, float load_current, float power_generated, float bus_voltage, float bat_current_setpoint, long runtime, int time_until_maintenance, int generator_speed, short rectifier_temperature, short generator_temperature, int sysid, int compid, boolean isMavlink2) {
        this.msgid = MAVLINK_MSG_ID_GENERATOR_STATUS;
        this.sysid = sysid;
        this.compid = compid;
        this.isMavlink2 = isMavlink2;

        this.status = status;
        this.battery_current = battery_current;
        this.load_current = load_current;
        this.power_generated = power_generated;
        this.bus_voltage = bus_voltage;
        this.bat_current_setpoint = bat_current_setpoint;
        this.runtime = runtime;
        this.time_until_maintenance = time_until_maintenance;
        this.generator_speed = generator_speed;
        this.rectifier_temperature = rectifier_temperature;
        this.generator_temperature = generator_temperature;
        
    }

    /**
     * Constructor for a new message, initializes the message with the payload
     * from a mavlink packet
     *
     */
    public msg_generator_status(MAVLinkPacket mavLinkPacket) {
        this.msgid = MAVLINK_MSG_ID_GENERATOR_STATUS;

        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.isMavlink2 = mavLinkPacket.isMavlink2;
        unpack(mavLinkPacket.payload);
    }

                          
    /**
     * Returns a string with the MSG name and data
     */
    @Override
    public String toString() {
        return "MAVLINK_MSG_ID_GENERATOR_STATUS - sysid:"+sysid+" compid:"+compid+" status:"+status+" battery_current:"+battery_current+" load_current:"+load_current+" power_generated:"+power_generated+" bus_voltage:"+bus_voltage+" bat_current_setpoint:"+bat_current_setpoint+" runtime:"+runtime+" time_until_maintenance:"+time_until_maintenance+" generator_speed:"+generator_speed+" rectifier_temperature:"+rectifier_temperature+" generator_temperature:"+generator_temperature+"";
    }

    /**
     * Returns a human-readable string of the name of the message
     */
    @Override
    public String name() {
        return "MAVLINK_MSG_ID_GENERATOR_STATUS";
    }
}
        