package com.onshop.yamuna.onshoppeeserver.Common;

import com.onshop.yamuna.onshoppeeserver.Models.Admin;

public class Common {
    public static Admin currentonlineUser;
    public static final String UserPhoneKey ="UserPhone";
    public static final String UserPasswordKey ="UserPassword";

    public static final String UPDATE="Update";
    public static final String DELETE="Delete";
    public static final int Pick_image=71;

    public static String convertStringtoStatus(String code){
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On the way";
        else
            return "Shipped";
    }

}
