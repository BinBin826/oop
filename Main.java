import java.io.*;
import java.util.*;

// ================== INTERFACE ==================
interface IFileHandler {
    void writeToFile(String filename) throws IOException;
    void readFromFile(String filename) throws IOException;
}

// ================== ABSTRACT CLASS ITEM ==================
abstract class Item {
    protected String id;
    protected String name;
    protected double price;
    protected static int count = 0;

    public Item(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        count++;
    }

    public static int getCount() { return count; }
    public abstract void displayInfo();
    public abstract String toFileString();

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
}

// ================== LỚP CON ITEM ==================
class Pen extends Item {
    private String color;
    public Pen(String id, String name, double price, String color) {
        super(id, name, price);
        this.color = color;
    }
    @Override
    public void displayInfo() {
        System.out.printf("| %-10s | %-12s | %-15s | %-10.2f | %-10s |\n",
                "Pen", id, name, price, color);
    }
    @Override
    public String toFileString() { return "Pen," + id + "," + name + "," + price + "," + color; }
}

class Notebook extends Item {
    private int pages;
    public Notebook(String id, String name, double price, int pages) {
        super(id, name, price);
        this.pages = pages;
    }
    @Override
    public void displayInfo() {
        System.out.printf("| %-10s | %-12s | %-15s | %-10.2f | %-10d |\n",
                "Notebook", id, name, price, pages);
    }
    @Override
    public String toFileString() { return "Notebook," + id + "," + name + "," + price + "," + pages; }
}

class Ruler extends Item {
    private int size;
    public Ruler(String id, String name, double price, int size) {
        super(id, name, price);
        this.size = size;
    }
    @Override
    public void displayInfo() {
        System.out.printf("| %-10s | %-12s | %-15s | %-10.2f | %-10d |\n",
                "Ruler", id, name, price, size);
    }
    @Override
    public String toFileString() { return "Ruler," + id + "," + name + "," + price + "," + size; }
}

class Calculator extends Item {
    private String version;
    public Calculator(String id, String name, double price, String version) {
        super(id, name, price);
        this.version = version;
    }
    @Override
    public void displayInfo() {
        System.out.printf("| %-10s | %-12s | %-15s | %-10.2f | %-10s |\n",
                "Calculator", id, name, price, version);
    }
    @Override
    public String toFileString() { return "Calculator," + id + "," + name + "," + price + "," + version; }
}

// ================== QUẢN LÝ VĂN PHÒNG PHẨM ==================
class StationeryManager implements IFileHandler {
    private ArrayList<Item> items = new ArrayList<>();

    public void addItem(Item item) { items.add(item); }

    public void displayAll() {
        if (items.isEmpty()) {
            System.out.println("\n>> Danh sach rong!");
            return;
        }
        System.out.println("\n" + "=".repeat(75));
        System.out.printf("| %-10s | %-12s | %-15s | %-10s | %-10s |\n",
                "Loai", "ID", "Ten", "Gia", "Thuoc tinh");
        System.out.println("-".repeat(75));
        for (Item i : items) i.displayInfo();
        System.out.println("=".repeat(75));
    }

    public Item searchById(String id) {
        for (Item i : items)
            if (i.getId().equalsIgnoreCase(id)) return i;
        return null;
    }

    public void removeById(String id) {
        Item item = searchById(id);
        if (item != null) {
            items.remove(item);
            System.out.println("\n>> Da xoa san pham: " + id);
        } else System.out.println("\n>> Khong tim thay!");
    }

    public void updateItem(String id, String newName, double newPrice) {
        Item item = searchById(id);
        if (item != null) {
            item.setName(newName);
            item.setPrice(newPrice);
            System.out.println("\n>> Cap nhat thanh cong!");
        } else System.out.println("\n>> Khong tim thay!");
    }

    @Override
    public void writeToFile(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Item i : items) bw.write(i.toFileString() + "\n");
        }
        System.out.println("\n>> Da ghi du lieu ra file: " + filename);
    }

    @Override
    public void readFromFile(String filename) throws IOException {
        items.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                switch (p[0]) {
                    case "Pen" -> addItem(new Pen(p[1], p[2], Double.parseDouble(p[3]), p[4]));
                    case "Notebook" -> addItem(new Notebook(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4])));
                    case "Ruler" -> addItem(new Ruler(p[1], p[2], Double.parseDouble(p[3]), Integer.parseInt(p[4])));
                    case "Calculator" -> addItem(new Calculator(p[1], p[2], Double.parseDouble(p[3]), p[4]));
                }
            }
        }
        System.out.println("\n>> Da doc du lieu tu file: " + filename);
    }
}

// ================== ABSTRACT CLASS BILL ==================
abstract class Bill {
    protected String billID, date, staffName;
    protected double totalAmount;
    protected static int count = 0;

    public Bill(String billID, String date, String staffName) {
        this.billID = billID;
        this.date = date;
        this.staffName = staffName;
        count++;
    }

    public static int getCount() { return count; }
    public abstract void input(Scanner sc);
    public abstract void output();
    public abstract double calculateTotal();
}

// ================== CÁC LỚP BILL ==================
class ImportBill extends Bill {
    private String supplier;
    private int quantity;
    private double importPrice;

    public ImportBill() { super("", "", ""); }

    @Override
    public void input(Scanner sc) {
        System.out.print("Ma HD: "); billID = sc.nextLine();
        System.out.print("Ngay: "); date = sc.nextLine();
        System.out.print("Nhan vien: "); staffName = sc.nextLine();
        System.out.print("Nha cung cap: "); supplier = sc.nextLine();
        System.out.print("So luong: "); quantity = sc.nextInt();
        System.out.print("Gia nhap: "); importPrice = sc.nextDouble(); sc.nextLine();
    }

    @Override
    public double calculateTotal() { return totalAmount = quantity * importPrice; }

    @Override
    public void output() {
        System.out.println("\n[Hoa don nhap hang]");
        System.out.printf("Ma: %-8s | Ngay: %-10s | NV: %-10s | Nha cung cap: %-10s | SL: %-5d | Gia: %-10.2f | Tong: %-10.2f\n",
                billID, date, staffName, supplier, quantity, importPrice, calculateTotal());
    }
}

class SalesBill extends Bill {
    private String customer;
    private int quantity;
    private double salePrice;

    public SalesBill() { super("", "", ""); }

    @Override
    public void input(Scanner sc) {
        System.out.print("Ma HD: "); billID = sc.nextLine();
        System.out.print("Ngay: "); date = sc.nextLine();
        System.out.print("Nhan vien: "); staffName = sc.nextLine();
        System.out.print("Khach hang: "); customer = sc.nextLine();
        System.out.print("So luong: "); quantity = sc.nextInt();
        System.out.print("Gia ban: "); salePrice = sc.nextDouble(); sc.nextLine();
    }

    @Override
    public double calculateTotal() { return totalAmount = quantity * salePrice; }

    @Override
    public void output() {
        System.out.println("\n[Hoa don ban hang]");
        System.out.printf("Ma: %-8s | Ngay: %-10s | NV: %-10s | Khach hang: %-10s | SL: %-5d | Gia: %-10.2f | Tong: %-10.2f\n",
                billID, date, staffName, customer, quantity, salePrice, calculateTotal());
    }
}

class ReturnBill extends Bill {
    private String reason;
    private int quantityReturned;
    private double refundPerItem;

    public ReturnBill() { super("", "", ""); }

    @Override
    public void input(Scanner sc) {
        System.out.print("Ma HD: "); billID = sc.nextLine();
        System.out.print("Ngay: "); date = sc.nextLine();
        System.out.print("Nhan vien: "); staffName = sc.nextLine();
        System.out.print("Ly do tra: "); reason = sc.nextLine();
        System.out.print("So luong tra: "); quantityReturned = sc.nextInt();
        System.out.print("Tien hoan/sp: "); refundPerItem = sc.nextDouble(); sc.nextLine();
    }

    @Override
    public double calculateTotal() { return totalAmount = quantityReturned * refundPerItem; }

    @Override
    public void output() {
        System.out.println("\n[Hoa don tra hang]");
        System.out.printf("Ma: %-8s | Ngay: %-10s | NV: %-10s | Ly do: %-15s | SL: %-5d | Hoan: %-10.2f | Tong: %-10.2f\n",
                billID, date, staffName, reason, quantityReturned, refundPerItem, calculateTotal());
    }
}

// ================== BILL MANAGER ==================
class BillManager {
    private ArrayList<Bill> bills = new ArrayList<>();

    public void addBill(Bill b) { bills.add(b); }

    public void displayAll() {
        if (bills.isEmpty()) System.out.println("\n>> Danh sach hoa don rong!");
        else bills.forEach(Bill::output);
    }

    public void displayByType(Class<? extends Bill> type) {
        boolean found = false;
        for (Bill b : bills) {
            if (type.isInstance(b)) { b.output(); found = true; }
        }
        if (!found) System.out.println("\n>> Khong co hoa don loai nay!");
    }
}

// ================== NHÂN VIÊN ==================
class Employee {
    private String id;
    private String name;
    private String position;
    private double salary;

    public Employee(String id, String name, String position, double salary) {
        this.id = id; this.name = name; this.position = position; this.salary = salary;
    }

    public String getId() { return id; }
    public void setName(String name) { this.name = name; }
    public void setPosition(String position) { this.position = position; }
    public void setSalary(double salary) { this.salary = salary; }

    public void displayInfo() {
        System.out.printf("| %-12s | %-15s | %-12s | %-10.2f |\n", id, name, position, salary);
    }

    public String toFileString() { return id + "," + name + "," + position + "," + salary; }
}

// ================== QUẢN LÝ NHÂN VIÊN ==================
class EmployeeManager implements IFileHandler {
    private ArrayList<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee e) { employees.add(e); }

    public void displayAll() {
        if (employees.isEmpty()) System.out.println("\n>> Danh sach nhan vien rong!");
        else {
            System.out.println("\n" + "=".repeat(60));
            System.out.printf("| %-12s | %-15s | %-12s | %-10s |\n", "ID", "Ten", "Chuc vu", "Luong");
            System.out.println("-".repeat(60));
            for (Employee e : employees) e.displayInfo();
            System.out.println("=".repeat(60));
        }
    }

    public Employee searchById(String id) {
        for (Employee e : employees)
            if (e.getId().equalsIgnoreCase(id)) return e;
        return null;
    }

    public void removeById(String id) {
        Employee e = searchById(id);
        if (e != null) { employees.remove(e); System.out.println("\n>> Da xoa nhan vien: " + id); }
        else System.out.println("\n>> Khong tim thay!");
    }

    public void updateEmployee(String id, String newName, String newPosition, double newSalary) {
        Employee e = searchById(id);
        if (e != null) { e.setName(newName); e.setPosition(newPosition); e.setSalary(newSalary);
            System.out.println("\n>> Cap nhat thanh cong!"); }
        else System.out.println("\n>> Khong tim thay!");
    }

    @Override
    public void writeToFile(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Employee e : employees) bw.write(e.toFileString() + "\n");
        }
        System.out.println("\n>> Da ghi danh sach nhan vien ra file: " + filename);
    }

    @Override
    public void readFromFile(String filename) throws IOException {
        employees.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                addEmployee(new Employee(p[0], p[1], p[2], Double.parseDouble(p[3])));
            }
        }
        System.out.println("\n>> Da doc danh sach nhan vien tu file: " + filename);
    }
}

// ================== HỖ TRỢ HIỂN THỊ ==================
class ConsoleHelper {
    public static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.printf("%30s\n", title);
        System.out.println("=".repeat(60));
    }

    public static void printSubHeader(String title) {
        System.out.println("\n" + "-".repeat(60));
        System.out.printf("%30s\n", title);
        System.out.println("-".repeat(60));
    }

    public static void printMessage(String msg) {
        System.out.println("\n>> " + msg);
    }

    public static void pressEnterToContinue(Scanner sc) {
        System.out.println("\nNhan Enter de tiep tuc...");
        sc.nextLine();
    }
}

// ================== MAIN ==================
public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        StationeryManager sm = new StationeryManager();
        BillManager bm = new BillManager();
        EmployeeManager em = new EmployeeManager();
        int menu;
        do {
            ConsoleHelper.printHeader("MENU CHINH");
            System.out.println("1. Quan ly van phong pham");
            System.out.println("2. Quan ly hoa don");
            System.out.println("3. Quan ly nhan vien");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            menu = sc.nextInt(); sc.nextLine();

            switch(menu){
                case 1 -> stationeryMenu(sm, sc);
                case 2 -> billMenu(bm, sc);
                case 3 -> employeeMenu(em, sc);
                case 0 -> ConsoleHelper.printMessage("Ket thuc chuong trinh!");
                default -> ConsoleHelper.printMessage("Lua chon khong hop le!");
            }
        } while(menu != 0);
        sc.close();
    }

    // ===== MENU VĂN PHÒNG PHẨM =====
    public static void stationeryMenu(StationeryManager sm, Scanner sc) throws IOException {
        String file = "stationery.txt";
        int choice;
        do {
            ConsoleHelper.printSubHeader("MENU SAN PHAM");
            System.out.println("1. Them san pham");
            System.out.println("2. Xem danh sach");
            System.out.println("3. Tim kiem theo ID");
            System.out.println("4. Cap nhat san pham");
            System.out.println("5. Xoa san pham");
            System.out.println("6. Ghi file");
            System.out.println("7. Doc file");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice = sc.nextInt(); sc.nextLine();

            switch(choice){
                case 1 -> {
                    System.out.print("Loai (1-Pen,2-Notebook,3-Ruler,4-Calculator): "); int type = sc.nextInt(); sc.nextLine();
                    System.out.print("ID: "); String id = sc.nextLine();
                    System.out.print("Ten: "); String name = sc.nextLine();
                    System.out.print("Gia: "); double price = sc.nextDouble(); sc.nextLine();
                    switch(type){
                        case 1 -> { System.out.print("Mau: "); String color = sc.nextLine(); sm.addItem(new Pen(id,name,price,color)); }
                        case 2 -> { System.out.print("So trang: "); int pages = sc.nextInt(); sc.nextLine(); sm.addItem(new Notebook(id,name,price,pages)); }
                        case 3 -> { System.out.print("Size: "); int size = sc.nextInt(); sc.nextLine(); sm.addItem(new Ruler(id,name,price,size)); }
                        case 4 -> { System.out.print("Version: "); String ver = sc.nextLine(); sm.addItem(new Calculator(id,name,price,ver)); }
                    }
                    ConsoleHelper.printMessage("Da them san pham!");
                }
                case 2 -> sm.displayAll();
                case 3 -> {
                    System.out.print("Nhap ID can tim: "); String id = sc.nextLine();
                    Item i = sm.searchById(id);
                    if(i != null) i.displayInfo();
                    else ConsoleHelper.printMessage("Khong tim thay!");
                }
                case 4 -> {
                    System.out.print("Nhap ID can cap nhat: "); String id = sc.nextLine();
                    System.out.print("Ten moi: "); String name = sc.nextLine();
                    System.out.print("Gia moi: "); double price = sc.nextDouble(); sc.nextLine();
                    sm.updateItem(id,name,price);
                }
                case 5 -> {
                    System.out.print("Nhap ID can xoa: "); String id = sc.nextLine();
                    sm.removeById(id);
                }
                case 6 -> sm.writeToFile(file);
                case 7 -> sm.readFromFile(file);
                case 0 -> ConsoleHelper.printMessage("Quay lai menu chinh...");
                default -> ConsoleHelper.printMessage("Lua chon khong hop le!");
            }
            ConsoleHelper.pressEnterToContinue(sc);
        } while(choice != 0);
    }

    // ===== MENU HÓA ĐƠN =====
    public static void billMenu(BillManager bm, Scanner sc) {
        int choice;
        do {
            ConsoleHelper.printSubHeader("MENU HOA DON");
            System.out.println("1. Them hoa don nhap");
            System.out.println("2. Them hoa don ban");
            System.out.println("3. Them hoa don tra");
            System.out.println("4. Xem tat ca hoa don");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice = sc.nextInt(); sc.nextLine();

            switch(choice){
                case 1 -> { ImportBill ib = new ImportBill(); ib.input(sc); bm.addBill(ib); }
                case 2 -> { SalesBill sb = new SalesBill(); sb.input(sc); bm.addBill(sb); }
                case 3 -> { ReturnBill rb = new ReturnBill(); rb.input(sc); bm.addBill(rb); }
                case 4 -> bm.displayAll();
                case 0 -> ConsoleHelper.printMessage("Quay lai menu chinh...");
                default -> ConsoleHelper.printMessage("Lua chon khong hop le!");
            }
            ConsoleHelper.pressEnterToContinue(sc);
        } while(choice != 0);
    }

    // ===== MENU NHÂN VIÊN =====
    public static void employeeMenu(EmployeeManager em, Scanner sc) throws IOException {
        String file = "employee.txt";
        int choice;
        do {
            ConsoleHelper.printSubHeader("MENU NHAN VIEN");
            System.out.println("1. Them nhan vien");
            System.out.println("2. Xem danh sach");
            System.out.println("3. Tim kiem theo ID");
            System.out.println("4. Cap nhat nhan vien");
            System.out.println("5. Xoa nhan vien");
            System.out.println("6. Ghi file");
            System.out.println("7. Doc file");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice = sc.nextInt(); sc.nextLine();

            switch(choice){
                case 1 -> {
                    System.out.print("ID: "); String id = sc.nextLine();
                    System.out.print("Ten: "); String name = sc.nextLine();
                    System.out.print("Chuc vu: "); String pos = sc.nextLine();
                    System.out.print("Luong: "); double sal = sc.nextDouble(); sc.nextLine();
                    em.addEmployee(new Employee(id,name,pos,sal));
                    ConsoleHelper.printMessage("Da them nhan vien!");
                }
                case 2 -> em.displayAll();
                case 3 -> {
                    System.out.print("Nhap ID: "); String id = sc.nextLine();
                    Employee e = em.searchById(id);
                    if(e != null) e.displayInfo();
                    else ConsoleHelper.printMessage("Khong tim thay!");
                }
                case 4 -> {
                    System.out.print("Nhap ID can cap nhat: "); String id = sc.nextLine();
                    System.out.print("Ten moi: "); String name = sc.nextLine();
                    System.out.print("Chuc vu moi: "); String pos = sc.nextLine();
                    System.out.print("Luong moi: "); double sal = sc.nextDouble(); sc.nextLine();
                    em.updateEmployee(id,name,pos,sal);
                }
                case 5 -> {
                    System.out.print("Nhap ID can xoa: "); String id = sc.nextLine();
                    em.removeById(id);
                }
                case 6 -> em.writeToFile(file);
                case 7 -> em.readFromFile(file);
                case 0 -> ConsoleHelper.printMessage("Quay lai menu chinh...");
                default -> ConsoleHelper.printMessage("Lua chon khong hop le!");
            }
            ConsoleHelper.pressEnterToContinue(sc);
        } while(choice != 0);
    }
}
