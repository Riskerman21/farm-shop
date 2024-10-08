package farm;

import java.util.List;
import farm.core.*;
import farm.inventory.product.data.*;

import farm.customer.*;

import farm.inventory.product.*;
import farm.sales.transaction.SpecialSaleTransaction;
import farm.sales.transaction.Transaction;

import farm.inventory.*;

/**
 * Execute the Farm MVP program.
 * This file is for you to execute your program,
 * it will not be marked.
 */
public class Main {

    /**
     * Start the farm program.
     * @param args Parameters to the program, currently not supported.
     */
    public static void main(String[] args)
        throws DuplicateCustomerException {
        AddressBook addressBook = new AddressBook();
        Customer customer = new Customer("Ali", 33651111, "UQ");
        addressBook.addCustomer(customer);
        for (String name : List.of("James", "Alex", "Lauren")) {
            addressBook.addCustomer(new Customer(name, 1234, "1st Street"));
        }
        System.out.println(addressBook.getAllRecords());

        //// -- Stage 1: Products + Transactions
        System.out.println("\n");
        System.out.println(new Milk(Quality.IRIDIUM));

        Transaction transaction = new Transaction(customer);
        for (int i = 0; i < 3; i++) {
            transaction.getAssociatedCustomer().getCart().addProduct(new Milk());
        }
        transaction.getAssociatedCustomer().getCart().addProduct(new Egg());
        transaction.getAssociatedCustomer().getCart().addProduct(new Milk());
        transaction.finalise();
        System.out.println("\n");
        System.out.println(transaction.getReceipt());
        transaction = new SpecialSaleTransaction(customer);
        for (int i = 0; i < 3; i++) {
            transaction.getAssociatedCustomer().getCart().addProduct(new Milk());
        }
        transaction.getAssociatedCustomer().getCart().addProduct(new Egg());
        transaction.getAssociatedCustomer().getCart().addProduct(new Milk());
        transaction.finalise();
        System.out.println("\n".repeat(3));
        System.out.println(transaction.getReceipt());

        // -- Stage 2 + 3: Combining them together

        Inventory inventory = new BasicInventory();
        boolean fancy = false;

        // Keep removed for Stage 2 but add when Stage 3 is done
        inventory = new FancyInventory();
        //fancy = true;

        for (Barcode barcode : List.of(Barcode.MILK, Barcode.EGG, Barcode.WOOL, Barcode.EGG)) {
            for (Quality quality : List.of(Quality.REGULAR, Quality.SILVER, Quality.REGULAR,
                    Quality.GOLD, Quality.REGULAR, Quality.REGULAR, Quality.IRIDIUM)) {
                inventory.addProduct(barcode, quality);
            }
        }

        FarmManager manager = new FarmManager(new Farm(inventory, addressBook),
                new ShopFront(), fancy);
        manager.run();


    }
}