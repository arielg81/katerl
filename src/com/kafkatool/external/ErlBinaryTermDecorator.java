package com.kafkatool.external;

import java.util.Map;
import java.lang.*;
import java.io.*;
import com.ericsson.otp.erlang.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ErlBinaryTermDecorator implements ICustomMessageDecorator {
  private ProcessBuilder pb;

  public ErlBinaryTermDecorator() {
    //this.pb = new ProcessBuilder();
  }

  @Override
  public String getDisplayName() { return "Erlang Binary Term"; }

  @Override
  public String decorate(String zookeeperHost, String brokerHost, String topic, long partitionId, long offset, byte[] msg, Map<String, String> reserved) {
    try {
      OtpInputStream otpInputStream = new OtpInputStream(msg);
      OtpErlangObject erlangObject = OtpErlangObject.decode(otpInputStream);
      return decode(erlangObject);
    } catch(Throwable t) {
      return "Error: " + t.toString();
    }
  }

  private String decode(OtpErlangObject msg) {
    if (msg instanceof OtpErlangAtom) {
        return msg.toString();
    } else if (msg instanceof OtpErlangString) {
      return msg.toString();
    } else if (msg instanceof OtpErlangBinary) {
        String out = new String(((OtpErlangBinary)msg).binaryValue());
        return "'" + out + "'";
    } else if (msg instanceof OtpErlangMap) {
        String mapString = Arrays.stream(((OtpErlangMap) msg).keys())
                .map(key -> {
                    String keyStr = decode(key);
                    String value = decode(((OtpErlangMap)msg).get(key));
                    return keyStr + ":" + value;
                })
                .collect(Collectors.joining(","));
        return "{" + mapString + "}";
     } else if (msg instanceof OtpErlangList) {
        OtpErlangList erlangList = (OtpErlangList)msg;
        String listString = "[";
        if(erlangList.arity() > 0){
          listString += decode(erlangList.elementAt(0));
          for(int j = 1;j<erlangList.arity();j++){
            listString += "," + decode(erlangList.elementAt(j));
          }	
        }
        return listString + "]";
    } else if (msg instanceof OtpErlangTuple) {
      OtpErlangTuple erlangList = (OtpErlangTuple)msg;
      String listString = "";
      if(erlangList.arity() > 0){
        listString += decode(erlangList.elementAt(0));
        for(int k = 1;k<erlangList.arity();k++){
          listString += "," + decode(erlangList.elementAt(k));
        }
        //listString=listString.replaceAll("\\\\\"", "\"");
        //listString=listString.replaceAll("\"{", "{");
        listString=listString.replaceAll("\'", "\\\\\"");
        return "\"{" + listString + "}\"";
      }
      return "{}";
    } else {
        return msg.toString();
    }
  }

}
