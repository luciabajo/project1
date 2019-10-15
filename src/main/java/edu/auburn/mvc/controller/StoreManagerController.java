package edu.auburn.mvc.controller;

import edu.auburn.mvc.model.ProductModel;
import edu.auburn.mvc.model.CustomerModel;
import edu.auburn.mvc.model.PurchaseModel;
import edu.auburn.mvc.model.SQLiteDataAdapter;
import edu.auburn.mvc.model.IDataAdapter;
import edu.auburn.mvc.view.StoreManagerView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoreManagerController {
    public AddButtonListerner addButtonListerner = new AddButtonListerner();
    public AddCustomerButtonListerner addCustomerButtonListerner = new AddCustomerButtonListerner();
    public AddPurchaseButtonListerner addPurchaseButtonListerner = new AddPurchaseButtonListerner();
    public CancelButtonListerner cancelButtonListerner = new CancelButtonListerner();

    ProductModel product;
    CustomerModel customer;
    PurchaseModel purchase;
    StoreManagerView apView;

    // Constructor.
    public StoreManagerController(ProductModel product, CustomerModel customer, PurchaseModel purchase, StoreManagerView apView) {
        this.product = product;
        this.customer = customer;
        this.purchase = purchase;
        this.apView = apView;
    }

    class AddButtonListerner implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            // Create a SQLiteDataAdapter to use the saveProduct() method.
            SQLiteDataAdapter adapter = new SQLiteDataAdapter();

            // Open the connection to the database because it's closed after saving each product.
            adapter.connect();

            // Populate the product object with the dataentry.
            try {
                product.mProductID = Integer.parseInt(apView.productIdText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Product ID is invalid!");
                return;
            }
            product.mName = apView.nameText.getText();
            try {
                product.mPrice = Double.parseDouble(apView.priceText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Price is invalid!");
                return;
            }
            try {
                product.mQuantity = Double.parseDouble(apView.quantityText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "mQuantity is invalid!");
                return;
            }

            // Save the new product to the database.
            if (adapter.saveProduct(product) == IDataAdapter.PRODUCT_SAVED_OK) {
                JOptionPane.showMessageDialog(null, "Product added:\n" +
                        "Product ID = " + product.mProductID + "\n" +
                        "Name = " + product.mName + "\n" +
                        "Price = " + product.mPrice + "\n" +
                        "Quantity = " + product.mQuantity);

                // As the product was successfully added, clear the GUI.
                apView.productIdText.setText("");
                apView.nameText.setText("");
                apView.priceText.setText("");
                apView.quantityText.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Product CANNOT be added:\n" +
                        "Product ID = " + product.mProductID + "\n" +
                        "Name = " + product.mName + "\n" +
                        "Price = " + product.mPrice + "\n" +
                        "Quantity = " + product.mQuantity);
            }

            // Close the database connection after saving each product.
            adapter.disconnect();
        }
    }

    class AddCustomerButtonListerner implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            // Create a SQLiteDataAdapter to use the saveCustomer() method.
            SQLiteDataAdapter adapter = new SQLiteDataAdapter();

            // Open the connection to the database because it's closed after saving each customer.
            adapter.connect();

            // Populate the customer object with the dataentry.
            try {
                customer.customerID = Integer.parseInt(apView.customerIdText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Customer ID is invalid!");
                return;
            }
            customer.name = apView.customerNameText.getText();
            try {
                customer.zipCode = Integer.parseInt(apView.zipCodeText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ZIP code is invalid!");
                return;
            }
            customer.phone = apView.phoneText.getText();

            // Save the new customer to the database.
            if (adapter.saveCustomer(customer) == IDataAdapter.CUSTOMER_SAVED_OK) {
                JOptionPane.showMessageDialog(null, "Customer added:\n" +
                        "Customer ID = " + customer.customerID + "\n" +
                        "Name = " + customer.name + "\n" +
                        "Zip Code = " + customer.zipCode + "\n" +
                        "Phone = " + customer.phone);

                // As the product was successfully added, clear the GUI.
                apView.customerIdText.setText("");
                apView.customerNameText.setText("");
                apView.zipCodeText.setText("");
                apView.phoneText.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Customer CANNOT be added:\n" +
                        "Customer ID = " + customer.customerID + "\n" +
                        "Name = " + customer.name + "\n" +
                        "Zip Code = " + customer.zipCode + "\n" +
                        "Phone = " + customer.phone);
            }

            // Close the database connection after saving each customer.
            adapter.disconnect();
        }
    }

    class AddPurchaseButtonListerner implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            // Create a SQLiteDataAdapter to use the saveCustomer() method.
            SQLiteDataAdapter adapter = new SQLiteDataAdapter();

            double purchaseTax = 0.0;
            double purchaseSubTotal = 0.0;
            double purchaseTotal = 0.0;
            double salesTax = 0.04; // 4%

            // Open the connection to the database because it's closed after saving each customer.
            adapter.connect();

            // Populate the customer object with the dataentry.
            try {
                purchase.purchaseID = Integer.parseInt(apView.purchaseIdText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Purchase ID is invalid!");
                return;
            }
            try {
                purchase.productID = Integer.parseInt(apView.purchaseProductIdText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Product ID is invalid!");
                return;
            }
            try {
                purchase.customerID = Integer.parseInt(apView.purchaseCustomerIdText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Customer ID is invalid!");
                return;
            }
            try {
                purchase.quantity = Integer.parseInt(apView.purchaseQuantityText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity is invalid!");
                return;
            }

            // Get the product name based on the productID.
            product = adapter.loadProduct(purchase.productID);

            // Get the customer name based on the customerID.
            customer = adapter.loadCustomer(purchase.customerID);

            // Calculate the purchase subtotal.
            purchaseSubTotal = product.mPrice * purchase.quantity;

            // Calculate the tax on the purchase.
            // https://www.wikihow.com/Calculate-Sales-Tax
            purchaseTax = purchaseSubTotal * salesTax;

            // Calculate the purchase total.
            purchaseTotal = purchaseSubTotal + purchaseTax;

            // Save the new purchase to the database.
            if (adapter.savePurchase(purchase) == IDataAdapter.PURCHASE_SAVED_OK) {
                JOptionPane.showMessageDialog(null, "Purchase Receipt (purchase added):\n" +
                        "Product Name = " + product.mName + "\n" +
                        "Quantity Purchased = " + purchase.quantity + "\n" +
                        "Customer Name = " + customer.name + "\n" +
                        "Purchase Tax = " + purchaseTax + "\n" +
                        "Purchase Total = " + purchaseTotal);

                // As the product was successfully added, clear the GUI.
                apView.purchaseIdText.setText("");
                apView.purchaseProductIdText.setText("");
                apView.purchaseCustomerIdText.setText("");
                apView.purchaseQuantityText.setText("");
            } else {
                JOptionPane.showMessageDialog(null, "Purchase CANNOT be added:\n" +
                        "Purchase ID = " + purchase.purchaseID + "\n" +
                        "Product ID = " + purchase.productID + "\n" +
                        "Customer ID = " + purchase.customerID + "\n" +
                        "Quantity = " + purchase.quantity);
            }

            // Close the database connection after saving each customer.
            adapter.disconnect();
        }
    }

    class CancelButtonListerner implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
        }
    }
}