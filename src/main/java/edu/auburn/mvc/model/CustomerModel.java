package edu.auburn.mvc.model;

public class CustomerModel {
    public int customerID;
    public String name;
    public int zipCode;
    public String phone;

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(customerID).append(",");
        sb.append("\"").append(customerID).append("\"").append(",");
        sb.append(customerID).append(",");
        sb.append(customerID).append(")");
        return sb.toString();
    }
}
