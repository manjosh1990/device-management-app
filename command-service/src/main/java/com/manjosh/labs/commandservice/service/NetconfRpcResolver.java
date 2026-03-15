package com.manjosh.labs.commandservice.service;

import com.manjosh.labs.devicecontracts.models.CommandType;
import org.springframework.stereotype.Component;

@Component
public class NetconfRpcResolver {

  public String resolve(final CommandType commandType) {
    return switch (commandType) {
      case GET_CONFIG ->
          "<get-config><source><running/></source></get-config>";
      case SET_CONFIG -> null;
      case REBOOT ->
          "<system-restart xmlns=\"urn:ietf:params:xml:ns:yang:ietf-system\"/>";
    };
  }
}
