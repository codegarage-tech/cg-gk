package com.reversecoder.gk7.customer.interfaces;

import com.reversecoder.gk7.customer.utility.AllConstants;

/**
 * Created by rashed on 3/29/16.
 */
public interface ServiceCommunication {
    public void getData(Object data,AllConstants.SERVICE_RETURN_TYPE returnType);
}
