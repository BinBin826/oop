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

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
}

// ================== LỚP CON CỦA ITEM ==================
class Pen extends Item {
    private String color;
    public Pen(String id, String name, double price, String color) {
        super(id, name, price);
        this.color = color;
    }
    @Override
    public void displayInfo() {
        System.out.println("Pen - ID: " + id + ", Name: " + name + ", Price: " + price + ", Color: " + color);
    }
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
        System.out.println("Notebook - ID: " + id + ", Name: " + name + ", Price: " + price + ", Pages: " + pages);
    }
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
        System.out.println("Ruler - ID: " + id + ", Name: " + name + ", Price: " + price + ", Size(cm): " + size);
    }
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
        System.out.println("Calculator - ID: " + id + ", Name: " + name + ", Price: " + price + ", Version: " + version);
    }
    public String toFileString() { return "Calculator," + id + "," + name + "," + price + "," + version; }
}

// ================== QUẢN LÝ VĂN PHÒNG PHẨM ==================
class StationeryManager implements IFileHandler {
    private ArrayList<Item> items = new ArrayList<>();

    public void addItem(Item item) { items.add(item); }
    public void displayAll() {
        if (items.isEmpty()) System.out.println("Danh sach rong!");
        else items.forEach(Item::displayInfo);
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
            System.out.println("Da xoa san pham: " + id);
        } else System.out.println("Khong tim thay!");
    }
    public void updateItem(String id, String newName, double newPrice) {
        Item item = searchById(id);
        if (item != null) {
            item.setName(newName);
            item.setPrice(newPrice);
            System.out.println("Cap nhat thanh cong!");
        } else System.out.println("Khong tim thay!");
    }

    @Override
    public void writeToFile(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Item i : items) {
                if (i instanceof Pen) bw.write(((Pen) i).toFileString());
                else if (i instanceof Notebook) bw.write(((Notebook) i).toFileString());
                else if (i instanceof Ruler) bw.write(((Ruler) i).toFileString());
                else if (i instanceof Calculator) bw.write(((Calculator) i).toFileString());
                bw.newLine();
            }
        }
        System.out.println("Da ghi du lieu ra file: " + filename);
        File f = new File(filename);
System.out.println("→ Đường dẫn tuyệt đối của file: " + f.getAbsolutePath());

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
        System.out.println("Da doc du lieu tu file: " + filename);
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
    public String getBillID() { return billID; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public void setDate(String date) { this.date = date; }
}

// ================== CÁC LỚP CON CỦA BILL ==================
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
        System.out.print("Gia nhap: "); importPrice = sc.nextDouble();
        sc.nextLine(); // quan trọng để tránh lỗi nextLine sau nextInt/Double
    }

    @Override
    public double calculateTotal() { totalAmount = quantity * importPrice; return totalAmount; }

    @Override
    public void output() {
        System.out.println("\n[Hoa don nhap hang]");
        System.out.println("Ma: " + billID + " | Ngay: " + date + " | NV: " + staffName);
        System.out.println("Nha cung cap: " + supplier + " | SL: " + quantity + " | Gia: " + importPrice);
        System.out.println("=> Tong tien: " + calculateTotal());
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
        System.out.print("Gia ban: "); salePrice = sc.nextDouble();
        sc.nextLine();
    }

    @Override
    public double calculateTotal() { totalAmount = quantity * salePrice; return totalAmount; }

    @Override
    public void output() {
        System.out.println("\n[Hoa don ban hang]");
        System.out.println("Ma: " + billID + " | Ngay: " + date + " | NV: " + staffName);
        System.out.println("Khach hang: " + customer + " | SL: " + quantity + " | Gia: " + salePrice);
        System.out.println("=> Tong tien: " + calculateTotal());
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
        System.out.print("Tien hoan/sp: "); refundPerItem = sc.nextDouble();
        sc.nextLine();
    }

    @Override
    public double calculateTotal() { totalAmount = quantityReturned * refundPerItem; return totalAmount; }

    @Override
    public void output() {
        System.out.println("\n[Hoa don tra hang]");
        System.out.println("Ma: " + billID + " | Ngay: " + date + " | NV: " + staffName);
        System.out.println("Ly do: " + reason + " | SL: " + quantityReturned + " | Hoan/sp: " + refundPerItem);
        System.out.println("=> Tong tien hoan: " + calculateTotal());
    }
}

// ================== BILL MANAGER ==================
class BillManager {
    private ArrayList<Bill> bills = new ArrayList<>();

    public void addBill(Bill b) { bills.add(b); }

    public void displayAll() {
        if (bills.isEmpty()) System.out.println("Danh sach rong!");
        else bills.forEach(Bill::output);
    }

    // Hiển thị theo loại hóa đơn
    public void displayByType(Class<? extends Bill> type) {
        boolean found = false;
        for (Bill b : bills) {
            if (type.isInstance(b)) {
                b.output();
                found = true;
            }
        }
        if (!found) System.out.println("Khong co hoa don loai nay!");
    }
}

// ================== LỚP NHÂN VIÊN ==================
class Employee {
    private String id;
    private String name;
    private String position;
    private double salary;

    public Employee(String id, String name, String position, double salary) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public double getSalary() { return salary; }

    public void setName(String name) { this.name = name; }
    public void setPosition(String position) { this.position = position; }
    public void setSalary(double salary) { this.salary = salary; }

    public void displayInfo() {
        System.out.println("ID: " + id + " | Name: " + name + " | Position: " + position + " | Salary: " + salary);
    }

    public String toFileString() {
        return id + "," + name + "," + position + "," + salary;
    }
}

// ================== QUẢN LÝ NHÂN VIÊN ==================
class EmployeeManager implements IFileHandler {
    private ArrayList<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee e) { employees.add(e); }

    public void displayAll() {
        if (employees.isEmpty()) System.out.println("Danh sach nhan vien rong!");
        else employees.forEach(Employee::displayInfo);
    }

    public Employee searchById(String id) {
        for (Employee e : employees)
            if (e.getId().equalsIgnoreCase(id)) return e;
        return null;
    }

    public void removeById(String id) {
        Employee e = searchById(id);
        if (e != null) {
            employees.remove(e);
            System.out.println("Da xoa nhan vien: " + id);
        } else System.out.println("Khong tim thay!");
    }

    public void updateEmployee(String id, String newName, String newPosition, double newSalary) {
        Employee e = searchById(id);
        if (e != null) {
            e.setName(newName);
            e.setPosition(newPosition);
            e.setSalary(newSalary);
            System.out.println("Cap nhat thanh cong!");
        } else System.out.println("Khong tim thay!");
    }

    @Override
    public void writeToFile(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Employee e : employees) {
                bw.write(e.toFileString());
                bw.newLine();
            }
        }
        System.out.println("Da ghi danh sach nhan vien ra file: " + filename);
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
        System.out.println("Da doc danh sach nhan vien tu file: " + filename);
    }
}


// ================== MAIN ==================
public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in); // duy nhất
        StationeryManager sm = new StationeryManager();
        BillManager bm = new BillManager();
        EmployeeManager em = new EmployeeManager();
        
        int menu;
        do {
            System.out.println("\n===== MENU CHINH =====");
            System.out.println("1. Quan ly van phong pham");
            System.out.println("2. Quan ly hoa don");
            System.out.println("3. Quan ly nhan vien");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            menu = sc.nextInt();
            sc.nextLine();

            switch (menu) {
                case 1 -> stationeryMenu(sm, sc);
                case 2 -> billMenu(bm, sc);
                case 3 -> employeeMenu(em, sc);
                case 0 -> System.out.println("Ket thuc chuong trinh!");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (menu != 0);

        sc.close(); // chỉ đóng 1 lần ở cuối
    }

    // ===== MENU VĂN PHÒNG PHẨM =====
    public static void stationeryMenu(StationeryManager sm, Scanner sc) throws IOException {
        String file = "stationery.txt";
        int choice;
        do {
            System.out.println("\n--- MENU SAN PHAM ---");
            System.out.println("1. Them");
            System.out.println("2. Xem danh sach");
            System.out.println("3. Tim kiem");
            System.out.println("4. Cap nhat");
            System.out.println("5. Xoa");
            System.out.println("6. Ghi file");
            System.out.println("7. Doc file");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Loai (1-Pen, 2-Notebook, 3-Ruler, 4-Calculator): ");
                    int type = sc.nextInt(); sc.nextLine();
                    System.out.print("ID: "); String id = sc.nextLine();
                    System.out.print("Ten: "); String name = sc.nextLine();
                    System.out.print("Gia: "); double price = sc.nextDouble(); sc.nextLine();

                    switch (type) {
                        case 1 -> { System.out.print("Mau: "); String color = sc.nextLine(); sm.addItem(new Pen(id, name, price, color)); }
                        case 2 -> { System.out.print("So trang: "); int pages = sc.nextInt(); sc.nextLine(); sm.addItem(new Notebook(id, name, price, pages)); }
                        case 3 -> { System.out.print("Chieu dai(cm): "); int size = sc.nextInt(); sc.nextLine(); sm.addItem(new Ruler(id, name, price, size)); }
                        case 4 -> { System.out.print("Phien ban: "); String ver = sc.nextLine(); sm.addItem(new Calculator(id, name, price, ver)); }
                    }
                }
                case 2 -> sm.displayAll();
                case 3 -> { System.out.print("Nhap ID: "); String id = sc.nextLine(); Item found = sm.searchById(id); if (found != null) found.displayInfo(); else System.out.println("Khong tim thay!"); }
                case 4 -> { System.out.print("ID can sua: "); String id = sc.nextLine(); System.out.print("Ten moi: "); String name = sc.nextLine(); System.out.print("Gia moi: "); double price = sc.nextDouble(); sc.nextLine(); sm.updateItem(id, name, price); }
                case 5 -> { System.out.print("ID can xoa: "); String id = sc.nextLine(); sm.removeById(id); }
                case 6 -> sm.writeToFile(file);
                case 7 -> sm.readFromFile(file);
            }
        } while (choice != 0);
    }

    // ===== MENU HÓA ĐƠN =====

    public static void billMenu(BillManager bm, Scanner sc) {
    int choice;
    do {
        System.out.println("\n--- MENU HOA DON ---");
        System.out.println("1. Them hoa don nhap");
        System.out.println("2. Them hoa don ban");
        System.out.println("3. Them hoa don tra");
        System.out.println("4. Xem hoa don"); // menu con xem hóa đơn
        System.out.println("0. Thoat");
        System.out.print("Chon: ");
        choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> { Bill b = new ImportBill(); b.input(sc); bm.addBill(b); }
            case 2 -> { Bill b = new SalesBill(); b.input(sc); bm.addBill(b); }
            case 3 -> { Bill b = new ReturnBill(); b.input(sc); bm.addBill(b); }
            case 4 -> viewBillMenu(bm, sc); // gọi menu con
        }
    } while (choice != 0);
}

// ===== MENU CON XEM HOÁ ĐƠN =====
public static void viewBillMenu(BillManager bm, Scanner sc) {
    int choice;
    do {
        System.out.println("\n--- MENU XEM HOA DON ---");
        System.out.println("1. Xem hoa don nhap");
        System.out.println("2. Xem hoa don ban");
        System.out.println("3. Xem hoa don tra");
        System.out.println("4. Xem tat ca hoa don");
        System.out.println("0. Thoat");
        System.out.print("Chon: ");
        choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> bm.displayByType(ImportBill.class);
            case 2 -> bm.displayByType(SalesBill.class);
            case 3 -> bm.displayByType(ReturnBill.class);
            case 4 -> bm.displayAll();
            }
        } while (choice != 0);
    }


    // ================== MENU NHÂN VIÊN ==================
public static void employeeMenu(EmployeeManager em, Scanner sc) throws IOException {
    String file = "employees.txt";
    int choice;
    do {
        System.out.println("\n--- MENU NHAN VIEN ---");
        System.out.println("1. Them nhan vien");
        System.out.println("2. Xem danh sach");
        System.out.println("3. Tim kiem theo ID");
        System.out.println("4. Cap nhat");
        System.out.println("5. Xoa");
        System.out.println("6. Ghi file");
        System.out.println("7. Doc file");
        System.out.println("0. Thoat");
        System.out.print("Chon: ");
        choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("ID: "); String id = sc.nextLine();
                System.out.print("Ten: "); String name = sc.nextLine();
                System.out.print("Chuc vu: "); String pos = sc.nextLine();
                System.out.print("Luong: "); double salary = sc.nextDouble(); sc.nextLine();
                em.addEmployee(new Employee(id, name, pos, salary));
            }
            case 2 -> em.displayAll();
            case 3 -> {
                System.out.print("Nhap ID: "); String id = sc.nextLine();
                Employee e = em.searchById(id);
                if (e != null) e.displayInfo();
                else System.out.println("Khong tim thay!");
            }
            case 4 -> {
                System.out.print("ID can cap nhat: "); String id = sc.nextLine();
                System.out.print("Ten moi: "); String name = sc.nextLine();
                System.out.print("Chuc vu moi: "); String pos = sc.nextLine();
                System.out.print("Luong moi: "); double salary = sc.nextDouble(); sc.nextLine();
                em.updateEmployee(id, name, pos, salary);
            }
            case 5 -> {
                System.out.print("ID can xoa: "); String id = sc.nextLine();
                em.removeById(id);
            }
            case 6 -> em.writeToFile(file);
            case 7 -> em.readFromFile(file);
        }
    } while (choice != 0);
}

}
