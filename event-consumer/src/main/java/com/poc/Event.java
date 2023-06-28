// automatically generated by the FlatBuffers compiler, do not modify

package com.poc;

import com.google.flatbuffers.BaseVector;
import com.google.flatbuffers.BooleanVector;
import com.google.flatbuffers.ByteVector;
import com.google.flatbuffers.Constants;
import com.google.flatbuffers.DoubleVector;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.FloatVector;
import com.google.flatbuffers.IntVector;
import com.google.flatbuffers.LongVector;
import com.google.flatbuffers.ShortVector;
import com.google.flatbuffers.StringVector;
import com.google.flatbuffers.Struct;
import com.google.flatbuffers.Table;
import com.google.flatbuffers.UnionVector;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings("unused")
public final class Event extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_23_5_26(); }
  public static Event getRootAsEvent(ByteBuffer _bb) { return getRootAsEvent(_bb, new Event()); }
  public static Event getRootAsEvent(ByteBuffer _bb, Event obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public Event __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public String satelliteName() { int o = __offset(4); return o != 0 ? __string(o + bb_pos) : null; }
  public ByteBuffer satelliteNameAsByteBuffer() { return __vector_as_bytebuffer(4, 1); }
  public ByteBuffer satelliteNameInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 4, 1); }
  public long timestampNs() { int o = __offset(6); return o != 0 ? bb.getLong(o + bb_pos) : 0L; }
  public double tempC() { int o = __offset(8); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public double batteryChargePct() { int o = __offset(10); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public double altitudeKm() { int o = __offset(12); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public double sensor1() { int o = __offset(14); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public double sensor2() { int o = __offset(16); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public double sensor3() { int o = __offset(18); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }

  public static int createEvent(FlatBufferBuilder builder,
      int satelliteNameOffset,
      long timestampNs,
      double tempC,
      double batteryChargePct,
      double altitudeKm,
      double sensor1,
      double sensor2,
      double sensor3) {
    builder.startTable(8);
    Event.addSensor3(builder, sensor3);
    Event.addSensor2(builder, sensor2);
    Event.addSensor1(builder, sensor1);
    Event.addAltitudeKm(builder, altitudeKm);
    Event.addBatteryChargePct(builder, batteryChargePct);
    Event.addTempC(builder, tempC);
    Event.addTimestampNs(builder, timestampNs);
    Event.addSatelliteName(builder, satelliteNameOffset);
    return Event.endEvent(builder);
  }

  public static void startEvent(FlatBufferBuilder builder) { builder.startTable(8); }
  public static void addSatelliteName(FlatBufferBuilder builder, int satelliteNameOffset) { builder.addOffset(0, satelliteNameOffset, 0); }
  public static void addTimestampNs(FlatBufferBuilder builder, long timestampNs) { builder.addLong(1, timestampNs, 0L); }
  public static void addTempC(FlatBufferBuilder builder, double tempC) { builder.addDouble(2, tempC, 0.0); }
  public static void addBatteryChargePct(FlatBufferBuilder builder, double batteryChargePct) { builder.addDouble(3, batteryChargePct, 0.0); }
  public static void addAltitudeKm(FlatBufferBuilder builder, double altitudeKm) { builder.addDouble(4, altitudeKm, 0.0); }
  public static void addSensor1(FlatBufferBuilder builder, double sensor1) { builder.addDouble(5, sensor1, 0.0); }
  public static void addSensor2(FlatBufferBuilder builder, double sensor2) { builder.addDouble(6, sensor2, 0.0); }
  public static void addSensor3(FlatBufferBuilder builder, double sensor3) { builder.addDouble(7, sensor3, 0.0); }
  public static int endEvent(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }
  public static void finishEventBuffer(FlatBufferBuilder builder, int offset) { builder.finish(offset); }
  public static void finishSizePrefixedEventBuffer(FlatBufferBuilder builder, int offset) { builder.finishSizePrefixed(offset); }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public Event get(int j) { return get(new Event(), j); }
    public Event get(Event obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

