package com.kafkatool.external;

import java.util.Map;

public interface ICustomMessageDecorator {
  public String getDisplayName();
  public String decorate(String zookeeperHost, String brokerHost, String topic, long partitionId, long offset, byte[] msg, Map<String, String> reserved);
}
