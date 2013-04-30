package com.digitalxyncing.communication;

import java.util.List;

/**
 * User: brian_anderson
 * Date: 4/29/13
 * <p/>
 * Add some readme here about how this operates
 */
public interface Sniffy {
    /**
     * Discovers the other digital xyncing services and returns
     * a list of ip addresses..
     *
     * @param portNumber
     * @return list of ip addresses
     */
    List<String> discoverXyncersOnPort(int portNumber);

}
