package invoicesystem.ui;

import java.util.*;
import invoicesystem.DatabaseConnection;
import invoicesystem.dao.*;
import invoicesystem.model.*;
import invoicesystem.util.*;


public class UserInterface {
    private CustomerDao customerDao;
    private LocationDao locationDao;
    private InvoiceDao invoiceDao;
    private ContractDao contractDao;
    private WorkDao workDao;
    private ProductDao productDao;
    private ReportFormatter reportFormatter;

    public UserInterface(DatabaseConnection connection) {
        this.customerDao = new CustomerDao(connection);
        this.locationDao = new LocationDao(connection);
        this.invoiceDao = new InvoiceDao(connection);
        this.contractDao = new ContractDao(connection);
        this.workDao = new WorkDao(connection);
        this.productDao = new ProductDao(connection);
        this.reportFormatter = new ReportFormatter();
    }

    public void run(Scanner scanner) {
        while (true) {
            System.out.println("Komennot:\n" +
                               "11 - listaa asiakkaat\n" +
                               "12 - listaa työkohteet\n" +
                               "13 - listaa sopimukset\n" +
                               "14 - listaa laskut\n\n" +

                               "21 - lisää asiakas\n" +
                               "22 - lisää työkohde\n" +
                               "23 - lisää laskutettavia työkohteelle\n\n" +

                               "31 - laske hinta-arvio kohteeseen\n\n" +

                               "41 - muodosta uusi tuntityölasku\n" +
                               "x - lopeta"
            );
            String command = scanner.nextLine();
            if (command.equals("x")) {
                break;
            }
            if (command.equals("11")) {
                listCustomers();
            }
            else if (command.equals("12")) {
                listLocations();
            }
            else if (command.equals("13")) {
                listContracts();
            }
            else if (command.equals("14")) {
                listInvoices();
            }
            else if (command.equals("21")) {
                addCustomer(scanner);
            }
            else if (command.equals("22")) {
                addLocation(scanner);
            }
            else if (command.equals("23")) {
                addItemsToContract(scanner);
            }
            else if (command.equals("31")) {
                getPriceEstimate(scanner);
            }
            else if (command.equals("41")) {
                createNewInvoice(scanner);
            }
        }
    }


    private int readInt(Scanner scanner, String message) {
        while (true) {
            System.out.println(message);
            String intInputStr = scanner.nextLine();
            try {
                int intInput = Integer.parseInt(intInputStr);
                return intInput;
            } catch (Exception e) {
                System.out.println("Syöte ei ole numero!");
            }
        }
    }

    private boolean readConfirm(Scanner scanner, String message) {
        String input = "";
        while (true) {
            System.out.println(message);
            input = scanner.nextLine().toLowerCase();
            if (input.equals("k") || input.equals("e")) {
                return (input.equals("k"));
            }
            System.out.println("Syötä 'k' tai 'e'.");
        }
    }

    private void listLocations() {
        System.out.println("\nTyökohteet:\n" + reportFormatter.getShortDivider());

        List<Location> locations = this.locationDao.list();

        locations.forEach(
            (location) -> System.out.println("\n" +
                location.toString() + "\n\n" + reportFormatter.getShortDivider()));
        System.out.println();
    }

    private void listCustomers() {
        System.out.println("\nAsiakkaat:\n" + reportFormatter.getShortDivider());

        List<Customer> customers = this.customerDao.list();
        customers.forEach(
            (customer) -> System.out.println("\n" +
                customer.toString() + "\n\n" + reportFormatter.getShortDivider()));
        System.out.println();
    }

    private void listContracts() {
        System.out.println("\nSopimukset:\n" + reportFormatter.getShortDivider());

        List<Contract> contracts = this.contractDao.list();

        contracts.forEach(
            (contract) -> System.out.println("\n" +
                contract.toString() + "\n\n" + reportFormatter.getShortDivider()));
        System.out.println();
    }

    private void listInvoices() {
        System.out.println("\nLaskut:\n" + reportFormatter.getLongDivider());

        List<Invoice> invoices = this.invoiceDao.list();

        invoices.forEach(
            (invoice) -> System.out.println("\n" +
                invoice.toString() + "\n\n" + reportFormatter.getLongDivider()));
        System.out.println();
    }

    private Customer getCustomer(Scanner scanner) {
        int customerId = readInt(scanner, "Anna asiakasnumero:");

        Customer customer = this.customerDao.read(customerId);
        return customer;
    }

    private Location getLocation(Scanner scanner) {
        int locationId = readInt(scanner, "Anna työkohteen tunnistenumero:");
        Location location = this.locationDao.read(locationId);
        return location;
    }

    private Customer addCustomer(Scanner scanner) {
        System.out.println("Lisätään asiakas");
        System.out.println();

        System.out.println("Anna asiakkaan nimi: ");
        String customerName = scanner.nextLine(); 

        System.out.println("Anna asiakkaan osoite: ");
        String customerAddress = scanner.nextLine();

        int id = this.customerDao.createNewId();
        Customer newCustomer = new Customer(id, customerName, customerAddress);
        this.customerDao.create(newCustomer);

        System.out.println("Uusi asiakas lisätty!");

        return newCustomer;
    }

    private void addLocation(Scanner scanner) {
        Customer customer = null;
        System.out.println("Lisätäänkö työkohde uudelle asiakkaalle? K/E");
        String addNewCustomer = scanner.nextLine();

        if (addNewCustomer.toLowerCase().equals("k")) {
            customer = addCustomer(scanner); 
        } else {
            while (customer == null) {
                customer = getCustomer(scanner);
                if (customer == null) {
                    System.out.println("Asiakasnumerolla ei löytynyt asiakasta!");
                    listCustomers();
                }
            }
        }
        System.out.println("Anna kohteen osoite:");
        String locationAddress = scanner.nextLine();

        String htcEligible = readConfirm(scanner, "Onko työ kotitalousvähennyskelpoinen? K/E")
                             ? "K" : "E";
        int id = this.locationDao.createNewId();
        Location location = new Location(id, customer, locationAddress, htcEligible);
        this.locationDao.create(location);
        
        if (readConfirm(scanner, "Luodaanko kohteelle uusi sopimus? K/E")) {
            createContractToLocation(scanner, location);
        }
    }

    private Contract selectContract(Scanner scanner, List<Contract> contracts) {
        Contract contract = null;
        System.out.println("Työkohteelle löytyi seuraavat sopimukset: ");
        contracts.forEach(
            (contr) -> System.out.println(
                contr.toString() + "\n\n-----------------\n"));

        while (contract == null) {
            int contractId = readInt(scanner, "Anna sopimuksen tunnistenumero:");
            contract = contracts.stream()
                       .filter(c -> contractId == c.getId())
                       .findAny()
                       .orElse(null);
            if (contract == null) {
                System.out.println("Tunnisteella ei löydy sopimusta kohteelle.");
            }
        }
        return contract;
    }

    private List<Work> getListOfWorkToAdd (Scanner scanner, Contract contract) {
        List<Work> works = new ArrayList<Work>();

        if (!readConfirm(scanner, "Lisätäänkö tuntitöitä? K/E")) {
            return works;
        }
        while (true) {
            WorkType workType = null;
            Work work = null;

            List<WorkType> workTypes = this.workDao.listWorkTypes();
            if (workTypes.size() < 1) {
                System.out.println("Ei lisättäviä töitä saatavilla!");
                return works;
            }

            System.out.println("Anna työn tyypin numero seuraavista vaihtoehdoista:");
            for (int i = 0; i < workTypes.size(); i++) {
                WorkType type = workTypes.get(i);
                System.out.println(i + ": " + type.getWorkType());
            }

            while (workType == null) {
                int workTypeIndex = readInt(scanner, "");
                try {
                    workType = workTypes.get(workTypeIndex);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Anna numero työtyypin edestä.");
                }
            }

            int hours = readInt(scanner, "Anna tuntimäärä:");

            int discount = readInt(scanner, "Anna alennusprosentti (0-100)");

            work = new Work(workType, contract, hours, discount);
            works.add(work);

            if (!readConfirm(scanner, "Lisätäänkö lisää tuntitöitä? K/E")) {
                break;
            }
        }
        return works;
    }

    private List<BillableProduct> getListOfProductsToAdd(Scanner scanner, Contract contract) {
        List<BillableProduct> products = new ArrayList<BillableProduct>();

        if (!readConfirm(scanner, "Lisätäänkö tarvikkeita? K/E")) {
            return products;
        }

        while (true) {
            InventoryProduct invProduct = null;
            BillableProduct bilProduct = null;

            List<InventoryProduct> invProducts = this.productDao.listProductInventory();

            if (invProducts.size() < 1) {
                System.out.println("Ei lisättäviä tarvikkeita saatavilla!");
                return products;
            }

            System.out.println("Anna tarvikkeen tunnistenumero seuraavista vaihtoehdoista:");
            for (InventoryProduct prod : invProducts) {
                System.out.println(prod.getId() + ":" + prod.getName() + 
                                   "(varastosaldo: " + prod.getStock() + prod.getUnit() + ")");
            }

            while (invProduct == null) {
                int invProdId = readInt(scanner, "");
                invProduct = invProducts.stream()
                    .filter(prod -> invProdId == prod.getId())
                    .findAny()
                    .orElse(null);
                if (invProduct == null) {
                    System.out.println("Tunnisteella ei löydy tarviketta! Anna validi tunniste.");
                }
            }

            int count = readInt(scanner, "Anna lukumäärä (" + invProduct.getUnit() + "): ");

            int discount = readInt (scanner, "Anna alennusprosentti (0-100): ");

            bilProduct = new BillableProduct(contract, invProduct, count, discount);
            products.add(bilProduct);

            if (!readConfirm(scanner, "Lisätäänkö lisää tarvikkeita? K/E")) {
                break;
            }
        }
        return products;
    }

    private Contract createContractToLocation(Scanner scanner, Location loc) {
        Contract contract = null;
        String cType = readConfirm(scanner, "Onko kyseessä urakka- (syötä k) vai tuntityösopimus (syötä e)?")
                       ? "urakka" : "tuntityo";
        int paymentParts = readInt(scanner, "Syötä laskun erien määrä: ");

        int id = this.contractDao.createNewId();
        contract = new Contract(id, loc, cType, paymentParts);
        this.contractDao.create(contract);
        System.out.println("Uusi kohde lisätty!");
        return contract;
    }

    private Invoice createNewInvoice(Scanner scanner) {
        System.out.println("Lisätään uusi lasku.");
        System.out.println();

        int invoiceNum = readInt(scanner, "Anna sopimusnumero: "); 

        Contract contract = this.contractDao.read(invoiceNum);

        if (contract == null) {
            System.out.println("Numerolla ei löytynyt sopimusta.");
            listContracts();
            return null;
        }

        List<Invoice> contractInvoices = this.invoiceDao.listInvoicesForContract(contract);

        if (contractInvoices.size() > 0) {
            System.out.println("Sopimukselle on jo luotu lasku");
            return null;  // TODO: Mitä vaihtoehtoja?
        }

        int id = this.invoiceDao.createNewId();
        Calendar now = Calendar.getInstance();
        
        // TODO: billingDate from input
        java.sql.Date billingDate = new java.sql.Date(now.getTime().getTime());
        now.add(Calendar.WEEK_OF_MONTH, 2);
        java.sql.Date dueDate = new java.sql.Date(now.getTime().getTime());

        List<Work> works = this.invoiceDao.listContractWorks(contract);
        List<BillableProduct> products = this.invoiceDao.listContractProducts(contract);

        Invoice invoice = new Invoice(id, contract, "lasku", "valmis",
                                      billingDate, dueDate, 1, 1, null,
                                      "maksamatta", 0, 0, works, products);

        this.invoiceDao.create(invoice);

        System.out.println("Laskulla on seuraavat tiedot:");
        System.out.println(invoice.toString());

        return invoice;
    }

    private void addItemsToContract(Scanner scanner) {
        Location location = getLocation(scanner);
        Contract contract = null;
        List<Contract> locationContracts = contractDao.getContractsByLocation(location);
        if (locationContracts.size() == 1) {
            contract = locationContracts.get(0);
            if (!readConfirm(scanner, "Löytyi sopimus " + contract.getId() +
                                      ". Valitaanko tämä (K) vai luodaanko uusi (E)?")) {
                contract = createContractToLocation(scanner, location);
            }
        } else if (locationContracts.size() > 1) {
            contract = selectContract(scanner, locationContracts);
        }
        if (contract == null) {
            System.out.println("Ei sopimuksia työkohteella " + location.getId());
            if (readConfirm(scanner, "Luodaanko uusi sopimus?")) {
                contract = createContractToLocation(scanner, location);
            }
        }
        if (contract == null) {
            System.out.println("Ei valittua sopimusta, poistutaan.");
            return;
        }
        for (Work work : getListOfWorkToAdd(scanner, contract)) {
            this.workDao.create(work);
        }
        for (BillableProduct product : getListOfProductsToAdd(scanner, contract)) {
            this.productDao.create(product);
        }
    }

    private void getPriceEstimate(Scanner scanner) {
        Location location = getLocation(scanner);
        Contract contract = new Contract(-1, location, "tuntityo", 1);
        List<Work> works = getListOfWorkToAdd(scanner, contract);
        List<BillableProduct> products = getListOfProductsToAdd(scanner, contract);

        String productReport = this.reportFormatter.getProductReport(products);
        String workReport = this.reportFormatter.getWorkReport(works, location.getHtcEligible());

        System.out.println(workReport);
        System.out.println(productReport);
        System.out.println(reportFormatter.getLongDivider());
    }

}