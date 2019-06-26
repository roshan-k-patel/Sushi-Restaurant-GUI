package comp1206.sushi.server;

import comp1206.sushi.common.*;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Provides the Sushi Server user interface
 */
public class ServerWindow extends JFrame implements UpdateListener {

    private static final long serialVersionUID = -4661566573959270000L;
    private ServerInterface server;

    /**
     * Create a new server window
     *
     * @param server instance of server to interact with
     */
    public ServerWindow(ServerInterface server) {
        super("Sushi Server");
        this.server = server;
        this.setTitle(server.getRestaurantName() + " Server");
        server.addUpdateListener(this);
        MockServer mock = new MockServer();

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        startTimer();


        //Tab for orders                                                                ORDERS START
         JPanel textEntry = new JPanel();
         textEntry.setLayout(new GridLayout(4, 1));

          JTable ordTable = new JTable();

          Object[] columns = {"Name", "Distance", "Cost", "Status"};
          DefaultTableModel model = new DefaultTableModel();
          model.setColumnIdentifiers(columns);
          ordTable.setModel(model);

          JTextField textName = new JTextField();
          JTextField textDist = new JTextField();
          JTextField textCost = new JTextField();
          JTextField textStatus = new JTextField();


          JButton edit = new JButton("Edit");
          JButton delete = new JButton("Delete");
          JButton removeFinished = new JButton("Remove Completed Orders");


         JScrollPane textBox = new JScrollPane(ordTable);


          textEntry.add(textName);
          textEntry.add(textDist);
          textEntry.add(textCost);
          textEntry.add(textStatus);

           JPanel ordButtons = new JPanel();
           ordButtons.setLayout(new FlowLayout());
           ordButtons.add(delete);

          JPanel south = new JPanel();
          south.setLayout(new GridLayout(2, 1));
          south.add(ordButtons);

          JPanel orders = new JPanel();
          orders.setLayout(new BorderLayout());
          orders.add(textBox, BorderLayout.CENTER);
          orders.add(south, BorderLayout.SOUTH);

         int orderRow = 0;
         for (Order x : mock.orders) {
             try {


                model.addRow(new Order[]{x});
                model.setValueAt(x.getDistance(), orderRow, 1);
                model.setValueAt(mock.getOrderCost(x), orderRow, 2);
                model.setValueAt(mock.getOrderStatus(x), orderRow, 3);
                orderRow++;

                } catch (Exception e) {

               }
           }

             delete.addActionListener(new ActionListener() {
                   @Override
              public void actionPerformed(ActionEvent e) {
                  //removing from the table
                 int selectedRow = ordTable.getSelectedRow();

                 String selectedValue = model.getValueAt(selectedRow, 0).toString();

                 try {


                            for (Order x : mock.getOrders()) {


                          if (x.getName().equals(selectedValue)) {
                            try {

                                mock.removeOrder(x);
                            }

                            catch (Exception e2) {

                            }
                        }
                    }
                }

                catch (Exception e1) {

                }

                model.removeRow(selectedRow);

            }

        });

        //                                                                      ORDERS FINISHED


        //                                                                          DRONES START

          JTable droneTable = new JTable();

            Object[] columnsThree = {"Name", "Speed", "Progress", "Capacity", "Battery", "Source", "Destination", "Status"};
          DefaultTableModel modelThree = new DefaultTableModel();
          modelThree.setColumnIdentifiers(columnsThree);

            droneTable.setModel(modelThree);

           JTextField droneName = new JTextField();
            JTextField droneSpeed = new JTextField();
         JTextField droneProgress = new JTextField();
         JTextField droneCapacity = new JTextField();
            JTextField droneBattery = new JTextField();
            JTextField droneSource = new JTextField();
            JTextField droneDestination = new JTextField();

         JLabel dName = new JLabel("Name");
         JLabel dSpeed = new JLabel("Speed");
         JLabel dProgress = new JLabel("Progress");
         JLabel dCapacity = new JLabel("Capacity");
         JLabel dBattery = new JLabel("Battery");
         JLabel dSource = new JLabel("Source");
         JLabel dDestination = new JLabel("Destination");


          JButton droneAdd = new JButton("Add");
           JButton droneDelete = new JButton("Delete");


           JScrollPane textBoxDrone = new JScrollPane(droneTable);


          JPanel boxesThree = new JPanel();
         boxesThree.setLayout(new GridLayout(1, 2));
          // boxesThree.add(dName) ; boxesThree.add(droneName);
         boxesThree.add(dSpeed);
         boxesThree.add(droneSpeed);
         // boxesThree.add(dProgress) ; boxesThree.add(droneProgress);
         // boxesThree.add(dCapacity) ; boxesThree.add(droneCapacity);
         // boxesThree.add(dBattery) ; boxesThree.add(droneBattery);
         // boxesThree.add(dSource) ; boxesThree.add(droneSource);
         // boxesThree.add(dDestination) ; boxesThree.add(droneDestination);

          JPanel droneButtons = new JPanel();
          droneButtons.add(droneAdd, BorderLayout.SOUTH);
          droneButtons.add(droneDelete, BorderLayout.SOUTH);

         JPanel southThree = new JPanel(new GridLayout(2, 1)); //REMOVE THIS TO MAKE TEXT BOXES SMALLER
          southThree.add(boxesThree);
         southThree.add(droneButtons);


         JPanel drone = new JPanel();
         drone.setLayout(new BorderLayout());
         drone.add(textBoxDrone, BorderLayout.CENTER);
         drone.add(southThree, BorderLayout.SOUTH);

        int droneRow = 0;

        for (Drone x : mock.drones) {

            try {


                modelThree.addRow(new Drone[]{x});
                modelThree.setValueAt(x.getSpeed(), droneRow, 1);
                modelThree.setValueAt(x.getProgress(), droneRow, 2);
                modelThree.setValueAt(x.getCapacity(), droneRow, 3);
                modelThree.setValueAt(x.getBattery(), droneRow, 4);
                modelThree.setValueAt(x.getSource(), droneRow, 5);
                modelThree.setValueAt(x.getDestination(), droneRow, 6);
                modelThree.setValueAt(mock.getDroneStatus(x), droneRow, 7);

                droneRow++;


            }

            catch (Exception e) {

            }
        }


        droneAdd.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {


                try {


                    int dNumber = Integer.parseInt(droneSpeed.getText());
                    mock.addDrone(dNumber);
                }

                catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null,"Error: You must enter a number");
                }



                int rowCount = modelThree.getRowCount();
                for (int i = rowCount; i >= 0; i--) {
                    try {

                        modelThree.removeRow(i);
                    }

                    catch (Exception c) {

                    }


                }


                int droneButtonRow = 0;
                for (Drone x : mock.drones) {
                    try {


                        modelThree.addRow(new Drone[]{x});

                        modelThree.setValueAt(x.getSpeed(), droneButtonRow, 1);

                        modelThree.setValueAt(x.getProgress(), droneButtonRow, 2);

                        modelThree.setValueAt(x.getCapacity(), droneButtonRow, 3);

                        modelThree.setValueAt(x.getBattery(), droneButtonRow, 4);

                        modelThree.setValueAt(x.getSource(), droneButtonRow, 5);

                        modelThree.setValueAt(x.getDestination(), droneButtonRow, 6);

                        modelThree.setValueAt(mock.getDroneStatus(x), droneButtonRow, 7);

                        droneButtonRow++;
                    }

                    catch (Exception e1) {

                    }


                }
            }
        });


        droneDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //removing from the table
                int selectedRow = droneTable.getSelectedRow();

                String selectedValue = modelThree.getValueAt(selectedRow, 0).toString();

                try {

                    for (Drone x : mock.getDrones()) {
                        if (x.getName().equals(selectedValue)) {
                            try {
                                mock.removeDrone(x);
                            }
                            catch (Exception e1) {

                            }
                        }
                    }
                }

                catch (Exception e2) {

                }
                modelThree.removeRow(selectedRow);
            }
        });


        //                                                                          DRONES FINISHED

        //                                                                          STAFF START


         JTable staffTable = new JTable();

         Object[] columnsFour = {"Name", "Status", "Fatigue"};
         DefaultTableModel modelFour = new DefaultTableModel();
         modelFour.setColumnIdentifiers(columnsFour);

         staffTable.setModel(modelFour);

              JTextField staffName = new JTextField();
              JTextField staffStatus = new JTextField();
              JTextField staffFatigue = new JTextField();

              JLabel staffNameLabel = new JLabel("Name");
              JLabel staffStatusLabel = new JLabel("Status");
              JLabel staffFatigueLabel = new JLabel("Fatigue");


              JButton staffAdd = new JButton("Add");
              JButton staffDelete = new JButton("Delete");


              JScrollPane textBoxStaff = new JScrollPane(staffTable);


             JPanel boxesFour = new JPanel(new GridLayout(3, 2));
             boxesFour.add(staffNameLabel);
             boxesFour.add(staffName);
             //     boxesFour.add(staffStatusLabel)     ;boxesFour.add(staffStatus);
              //     boxesFour.add(staffFatigueLabel)    ;boxesFour.add(staffFatigue);

             JPanel staffButtons = new JPanel();
             staffButtons.add(staffAdd, BorderLayout.SOUTH);
             staffButtons.add(staffDelete, BorderLayout.SOUTH);


              JPanel southFour = new JPanel(new GridLayout(2, 1));
             southFour.add(boxesFour);
              southFour.add(staffButtons);


            JPanel staff = new JPanel();
             staff.setLayout(new BorderLayout());
             staff.add(textBoxStaff, BorderLayout.CENTER);
             staff.add(southFour, BorderLayout.SOUTH);


                  int staffRow = 0;
              for (Staff x : mock.staff) {

            try {

                modelFour.addRow(new Staff[]{x});
                modelFour.setValueAt(mock.getStaffStatus(x), staffRow, 1);
                modelFour.setValueAt(x.getFatigue(), staffRow, 2);

                staffRow++;

            }

            catch (Exception e) {

            }
        }

        staffAdd.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {

                boolean change = true;

                if (staffName.getText().equals("")){

                    JOptionPane.showMessageDialog(null,"The Name field cannot be empty");
                    change = false;
                }

                if(change){
                    mock.addStaff(staffName.getText());
                }

                //method to remove all rows
                int rowCount = modelFour.getRowCount();
                for (int i = rowCount; i >= 0; i--) {

                    try {

                        modelFour.removeRow(i);
                    }

                    catch (Exception c) {
                    }
                }

                int staffRow = 0;
                for (Staff x : mock.staff) {

                    try {

                        modelFour.addRow(new Staff[]{x});
                        modelFour.setValueAt(mock.getStaffStatus(x), staffRow, 1);
                        modelFour.setValueAt(x.getFatigue(), staffRow, 2);

                        staffRow++;

                    }

                    catch (Exception e1) {

                    }
                }

            }
        });

        staffDelete.addActionListener(new ActionListener() {

            @Override

            public void actionPerformed(ActionEvent e) {


                int selectedRow = staffTable.getSelectedRow();

                try {
                    String selectedValue = modelFour.getValueAt(selectedRow, 0).toString();

                    try {
                        for (Staff x : mock.getStaff()) {
                            if (x.getName().equals(selectedValue)) {
                                try {
                                    mock.removeStaff(x);
                                }

                                catch (Exception e2) {

                                }
                            }
                        }
                    }

                    catch (Exception e1) {

                    }

                     modelFour.removeRow(selectedRow);
                }

                catch (ArrayIndexOutOfBoundsException e2){

                    JOptionPane.showMessageDialog(null,"Error: Please select a row to delete");
                }

            }

        });

        //                                                                          STAFF FINISHED

        //                                                                          USERS START
           JPanel userButtons = new JPanel();

              JTable userTable = new JTable();

          Object[] columnsFive = {"Name", "Password", "Address", "Postcode"};
          DefaultTableModel modelFive = new DefaultTableModel();
          modelFive.setColumnIdentifiers(columnsFive);

          userTable.setModel(modelFive);

          JTextField userName = new JTextField();
          JTextField userPassword = new JTextField();
          JTextField userAddress = new JTextField();
          JTextField userPostcode = new JTextField();


          JButton userEdit = new JButton("Edit");
          JButton userRemove = new JButton("Remove");


          JScrollPane textBoxUsers = new JScrollPane(userTable);


          textBoxUsers.add(userName);
          textBoxUsers.add(userPassword);
          textBoxUsers.add(userAddress);
          textBoxUsers.add(userPostcode);



          JPanel users = new JPanel();
          users.setLayout(new BorderLayout());
          users.add(textBoxUsers, BorderLayout.CENTER);
          users.add(userButtons, BorderLayout.SOUTH);

          int userRow = 0;
          for (User x : mock.users) {

            try {

                modelFive.addRow(new User[]{x});
                modelFive.setValueAt(x.getPostcode(), userRow, 1);
                modelFive.setValueAt(x.getDistance(), userRow, 2);
                userRow++;

            }

            catch (Exception e) {

            }
        }

        //                                                                          USERS FINISHED


        //                                                                          DISHES START


          JTable dishTable = new JTable();

          Object[] columnsSix = {"Name", "Description", "Recipe", "Price", "Restock Threshold", "Restock Amount"};
          DefaultTableModel modelSix = new DefaultTableModel();
          modelSix.setColumnIdentifiers(columnsSix);

          dishTable.setModel(modelSix);

          JTextField dishName = new JTextField();

          JTextField dishDescription = new JTextField();

          JTextField dishPrice = new JTextField();

          JTextField dishRestockThreshold = new JTextField();

          JTextField dishRestockAmount = new JTextField();

          JComboBox<Dish> dishSelect = new JComboBox<>();

          JComboBox<Ingredient> ingredientSelect = new JComboBox<>();

          JTextField ingredientAmount = new JTextField();

        for (Dish x : mock.dishes){

            dishSelect.addItem(x);

        }

        for (Ingredient x : mock.ingredients){

            ingredientSelect.addItem(x);

        }


          JLabel dishNameLabel = new JLabel("Name");
          JLabel dishDescriptionLabel = new JLabel("Description");
          JLabel dishPriceLabel = new JLabel("Price");
          JLabel dishRestockThresholdLabel = new JLabel("Restock Threshold");
          JLabel dishRestockAmountLabel = new JLabel("Restock Amount");
          JLabel dishSelectLabel = new JLabel("Dish Selection");
          JLabel ingredientSelectLabel = new JLabel("Ingredient");
          JLabel ingredientAmountLabel = new JLabel("Amount");


        JButton dishCreate = new JButton("Create");
        JButton dishEdit = new JButton("Edit");
        JButton dishDelete = new JButton("Delete");
        JButton addIngredient = new JButton("Add Ingredient");
        JButton editIngredient = new JButton("Edit Ingredient");
        JButton deleteIngredient = new JButton("Delete Ingredient");


        JScrollPane textBoxDishes = new JScrollPane(dishTable);


        JPanel boxesSix = new JPanel(new GridLayout(8, 2));
        boxesSix.add(dishNameLabel);boxesSix.add(dishName);
        boxesSix.add(dishDescriptionLabel);boxesSix.add(dishDescription);
        boxesSix.add(dishPriceLabel);boxesSix.add(dishPrice);
        boxesSix.add(dishRestockThresholdLabel);boxesSix.add(dishRestockThreshold);
        boxesSix.add(dishRestockAmountLabel);boxesSix.add(dishRestockAmount);
        boxesSix.add(dishSelectLabel);boxesSix.add(dishSelect);
        boxesSix.add(ingredientSelectLabel);boxesSix.add(ingredientSelect);
        boxesSix.add(ingredientAmountLabel);boxesSix.add(ingredientAmount);

        JPanel dishButtons = new JPanel();
        dishButtons.add(dishCreate, BorderLayout.SOUTH);
        dishButtons.add(dishEdit, BorderLayout.SOUTH);
        dishButtons.add(dishDelete,BorderLayout.SOUTH);
        dishButtons.add(addIngredient,BorderLayout.SOUTH);
        dishButtons.add(editIngredient,BorderLayout.SOUTH);
        dishButtons.add(deleteIngredient,BorderLayout.SOUTH);


        JPanel southSix = new JPanel(new GridLayout(2, 1));
        southSix.add(boxesSix);
        southSix.add(dishButtons);


        JPanel dishes = new JPanel();
        dishes.setLayout(new BorderLayout());
        dishes.add(textBoxDishes, BorderLayout.CENTER);
        dishes.add(southSix, BorderLayout.SOUTH);


        int dishesRow = 0;
        for (Dish x : mock.dishes) {
            try {
                modelSix.addRow(new Dish[]{x});

                modelSix.setValueAt(x.getDescription(), dishesRow, 1);
                modelSix.setValueAt(mock.getRecipe(x), dishesRow, 2);
                modelSix.setValueAt(x.getPrice(), dishesRow, 3);
                modelSix.setValueAt(x.getRestockThreshold(), dishesRow, 4);
                modelSix.setValueAt(x.getRestockAmount(), dishesRow, 5);
                dishesRow++;

            }

            catch (Exception e) {

            }
        }

        dishCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    int dishPriceNum = Integer.parseInt(dishPrice.getText());
                    int dishRestockThresholdNum = Integer.parseInt(dishRestockThreshold.getText());
                    int dishRestockAmountNum = Integer.parseInt(dishRestockAmount.getText());
                    mock.addDish(dishName.getText(), dishDescription.getText(), dishPriceNum, dishRestockThresholdNum, dishRestockAmountNum);
                } catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null,"Error: Please ensure all fields have been filled correctly");
                }
                dishSelect.removeAllItems();

                for (Dish x : mock.dishes){
                    dishSelect.addItem(x);
                }

                int rowCount = modelSix.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    modelSix.removeRow(i);
                }

                int dishesRow = 0;
                for (Dish x : mock.dishes) {
                    try {
                        modelSix.addRow(new Dish[]{x});
                        modelSix.setValueAt(x.getDescription(), dishesRow, 1);
                        modelSix.setValueAt(mock.getRecipe(x), dishesRow, 2);
                        modelSix.setValueAt(x.getPrice(), dishesRow, 3);
                        modelSix.setValueAt(x.getRestockThreshold(), dishesRow, 4);
                        modelSix.setValueAt(x.getRestockAmount(), dishesRow, 5);
                        dishesRow++;

                    } catch (Exception e2) {

                    }
                }

            }
        });

        dishEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = dishTable.getSelectedRow();

                try {
                    modelSix.setValueAt(dishRestockThreshold.getText(), selectedRow, 3);
                    modelSix.setValueAt(dishRestockAmount.getText(), selectedRow, 4);

                } catch (ArrayIndexOutOfBoundsException e1){
                    JOptionPane.showMessageDialog(null,"Error: Please enter an integer for Restock Amount and Restock Threshold");
                }
                String selectedValue = modelSix.getValueAt(selectedRow, 0).toString();

                try {
                    for (Dish x : mock.getDishes()) {
                        if (x.getName().equals(selectedValue)) {
                            try {
                                mock.setRestockLevels(x,Integer.parseInt(dishRestockThreshold.getText()),Integer.parseInt(dishRestockAmount.getText()));
                            } catch (Exception e2) {

                            }
                        }
                    }
                } catch (Exception e1){

                }
            }
        });

        dishDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedRow = dishTable.getSelectedRow();

                try {
                    String selectedValue = modelSix.getValueAt(selectedRow, 0).toString();

                    try {
                        for (Dish x : mock.getDishes()) {

                            if (x.getName().equals(selectedValue)) {
                                try {
                                    mock.removeDish(x);
                                    mock.dishes.remove(x);
                                    modelSix.removeRow(selectedRow);
                                } catch (Exception e1) {

                                }
                            }
                        }
                    } catch (Exception e1) {

                    }
                }catch (ArrayIndexOutOfBoundsException e2){
                    JOptionPane.showMessageDialog(null,"Error: Please select a row to delete");
                }

            }
        });

        addIngredient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    mock.addIngredientToDish((Dish) dishSelect.getSelectedItem(), (Ingredient) ingredientSelect.getSelectedItem(), Integer.parseInt(ingredientAmount.getText()));
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null,"Error: Please input an integer amount");
                }

                int rowCount = modelSix.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    modelSix.removeRow(i);
                }


                int dishesRow = 0;
                for (Dish x : mock.dishes) {
                    try {

                        modelSix.addRow(new Dish[]{x});

                        modelSix.setValueAt(x.getDescription(), dishesRow, 1);

                        modelSix.setValueAt(mock.getRecipe(x), dishesRow, 2);

                        modelSix.setValueAt(x.getPrice(), dishesRow, 3);

                        modelSix.setValueAt(x.getRestockThreshold(), dishesRow, 4);

                        modelSix.setValueAt(x.getRestockAmount(), dishesRow, 5);

                        dishesRow++;

                    }

                    catch (Exception e1) {

                    }
                }


            }
        });

        deleteIngredient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mock.removeIngredientFromDish((Dish)dishSelect.getSelectedItem(),(Ingredient) ingredientSelect.getSelectedItem());

                int rowCount = modelSix.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    modelSix.removeRow(i);
                }


                int dishesRow = 0;
                for (Dish x : mock.dishes) {
                    try {

                        modelSix.addRow(new Dish[]{x});

                        modelSix.setValueAt(x.getDescription(), dishesRow, 1);

                        modelSix.setValueAt(mock.getRecipe(x), dishesRow, 2);

                        modelSix.setValueAt(x.getPrice(), dishesRow, 3);

                        modelSix.setValueAt(x.getRestockThreshold(), dishesRow, 4);

                        modelSix.setValueAt(x.getRestockAmount(), dishesRow, 5);

                        dishesRow++;

                    }

                    catch (Exception e1) {

                    }
                }
            }
        });


        editIngredient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    mock.addIngredientToDish((Dish) dishSelect.getSelectedItem(), (Ingredient) ingredientSelect.getSelectedItem(), Integer.parseInt(ingredientAmount.getText()));

                } catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null,"Error: Please input an integer amount");
                }


                int rowCount = modelSix.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    modelSix.removeRow(i);
                }


                int dishesRow = 0;
                for (Dish x : mock.dishes) {
                    try {

                        modelSix.addRow(new Dish[]{x});

                        modelSix.setValueAt(x.getDescription(), dishesRow, 1);

                        modelSix.setValueAt(mock.getRecipe(x), dishesRow, 2);

                        modelSix.setValueAt(x.getPrice(), dishesRow, 3);

                        modelSix.setValueAt(x.getRestockThreshold(), dishesRow, 4);

                        modelSix.setValueAt(x.getRestockAmount(), dishesRow, 5);

                        dishesRow++;

                    }

                    catch (Exception e1) {

                    }
                }
            }

        });



        //                                                                          DISHES FINISHED


        //                                                                          INGREDIENTS START


        JTable ingredientsTable = new JTable();

          Object[] columnsEight = {"Name", "Unit", "Supplier", "Restock Threshold", "Restock Amount"};
          DefaultTableModel modelEight = new DefaultTableModel();
         modelEight.setColumnIdentifiers(columnsEight);

        ingredientsTable.setModel(modelEight);

        JTextField ingredientName = new JTextField();
          JTextField ingredientUnit = new JTextField();
        JComboBox<Supplier> ingredientSupplier = new JComboBox();
          JTextField restockThreshold = new JTextField();
        JTextField restockAmount = new JTextField();

        for (Supplier x : mock.suppliers){
            ingredientSupplier.addItem(x);
        }

         JLabel ingredientNameLabel = new JLabel("Name");
          JLabel ingredientUnitLabel = new JLabel("Unit");
         JLabel ingredientSupplierLabel = new JLabel("Supplier");
        JLabel restockThresholdLabel = new JLabel("Restock Threshold");
        JLabel restockAmountLabel = new JLabel("Restock Amount");


        JButton ingredientAdd = new JButton("Create");
        JButton ingredientEdit = new JButton("Edit");
        JButton ingredientDelete = new JButton("Delete");


        JScrollPane textBoxIngredients = new JScrollPane(ingredientsTable);

        JPanel boxesEight = new JPanel(new GridLayout(5, 2));
        boxesEight.add(ingredientNameLabel);
        boxesEight.add(ingredientName);
        boxesEight.add(ingredientUnitLabel);
        boxesEight.add(ingredientUnit);
        boxesEight.add(ingredientSupplierLabel);
        boxesEight.add(ingredientSupplier);                        // CHANGE
        boxesEight.add(restockThresholdLabel);
        boxesEight.add(restockThreshold);
        boxesEight.add(restockAmountLabel);
        boxesEight.add(restockAmount);

        JPanel ingredientsButtons = new JPanel();
        ingredientsButtons.add(ingredientAdd);
        ingredientsButtons.add(ingredientEdit);
        ingredientsButtons.add(ingredientDelete);

        JPanel southEight = new JPanel(new GridLayout(2, 1));
        southEight.add(boxesEight);
        southEight.add(ingredientsButtons);

        JPanel ingredients = new JPanel();
        ingredients.setLayout(new BorderLayout());
        ingredients.add(textBoxIngredients, BorderLayout.CENTER);
        ingredients.add(southEight, BorderLayout.SOUTH);

        int ingredientsRow = 0;
        for (Ingredient x : mock.ingredients) {
            try {
                modelEight.addRow(new Ingredient[]{x});
                modelEight.setValueAt(x.getUnit(), ingredientsRow, 1);
                modelEight.setValueAt(x.getSupplier(), ingredientsRow, 2);
                modelEight.setValueAt(x.getRestockThreshold(), ingredientsRow, 3);
                modelEight.setValueAt(x.getRestockAmount(), ingredientsRow, 4);
                ingredientsRow++;

            } catch (Exception e) {

            }
        }

        ingredientAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    mock.addIngredient(ingredientName.getText(), ingredientUnit.getText(), (Supplier) ingredientSupplier.getSelectedItem(), Integer.parseInt(restockThreshold.getText()), Integer.parseInt(restockAmount.getText()));

                }  catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null,"Error: Please ensure all fields have been filled correctly");
                }
                ingredientSelect.removeAllItems();

                for (Ingredient x : mock.ingredients){
                    ingredientSelect.addItem(x);
                }

                int rowCount = modelEight.getRowCount();
                for (int i = rowCount; i >= 0; i--) {
                    try {
                        modelEight.removeRow(i);
                    } catch (Exception c) {

                    }

                }

                int ingredientsRow = 0;
                for (Ingredient x : mock.ingredients) {
                    try {

                        modelEight.addRow(new Ingredient[]{x});

                        modelEight.setValueAt(x.getUnit(), ingredientsRow, 1);

                        modelEight.setValueAt(x.getSupplier(), ingredientsRow, 2);

                        modelEight.setValueAt(x.getRestockThreshold(), ingredientsRow, 3);

                        modelEight.setValueAt(x.getRestockAmount(), ingredientsRow, 4);

                        ingredientsRow++;

                    }

                    catch (Exception e1) {

                    }
                }


            }
        });


        ingredientEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ingredientsTable.getSelectedRow();


                try {
                    modelEight.setValueAt(restockThreshold.getText(), selectedRow, 3);
                    modelEight.setValueAt(restockAmount.getText(), selectedRow, 4);

                }catch (ArrayIndexOutOfBoundsException e1){
                    JOptionPane.showMessageDialog(null,"Error: Please enter an integer into Restock Threshold and Restock Amount");
                }

                try {
                    String selectedValue = modelEight.getValueAt(selectedRow, 0).toString();


                    try {
                        for (Ingredient x : mock.getIngredients()) {
                            if (x.getName().equals(selectedValue)) {
                                try {
                                    mock.setRestockLevels(x, Integer.parseInt(restockThreshold.getText()), Integer.parseInt(restockAmount.getText()));
                                } catch (Exception e2) {
                                    JOptionPane.showMessageDialog(null,"Please enter an integer into Restock Threshold and Restock Amount ");
                                }
                            }
                        }
                    } catch (Exception e1) {

                    }
                } catch (ArrayIndexOutOfBoundsException e1){
                    JOptionPane.showMessageDialog(null,"Please select a row to edit");
                }
            }
        });

        ingredientDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ingredientsTable.getSelectedRow();



                try {
                    String selectedValue = modelEight.getValueAt(selectedRow, 0).toString();

                    boolean change = true;


                    int supplierRowCount = modelSix.getRowCount();
                    for (int i = 0; i < supplierRowCount; i++) {
                        if (modelSix.getValueAt(i, 2).toString().contains(selectedValue)) {
                            JOptionPane.showMessageDialog(null, "Error: This Ingredient cannot be deleted as it is used in a Dish Recipe");
                            change = false;
                        }
                    }

                    if (change) {
                        try {
                            for (Ingredient x : mock.getIngredients()) {

                                if (x.getName().equals(selectedValue)) {
                                    try {

                                        mock.removeIngredient(x);
                                        mock.ingredients.remove(x);
                                        modelEight.removeRow(selectedRow);

                                    }

                                    catch (Exception e1) {

                                    }
                                }
                            }
                        }

                        catch (Exception e1) {

                        }
                    }
                }

                catch (ArrayIndexOutOfBoundsException e1){

                    JOptionPane.showMessageDialog(null,"Error: Please select a row to delete");

                }

            }
        });


        //                                                                          INGREDIENTS FINISHED

        //                                                                          SUPPLIERS START


        JTable suppliersTable = new JTable();

        Object[] columnsSeven = {"Name", "Postcode", "Distance"};
        DefaultTableModel modelSeven = new DefaultTableModel();
        modelSeven.setColumnIdentifiers(columnsSeven);

        suppliersTable.setModel(modelSeven);

        JTextField supplierName = new JTextField();
        JComboBox<Postcode> supplierPostcode = new JComboBox();
        JTextField supplierDistance = new JTextField();

        for (Postcode x : mock.postcodes){
            supplierPostcode.addItem(x);
        }

        JLabel supplierNameLabel = new JLabel("Name");
        JLabel supplierPostcodeLabel = new JLabel("Postcode");
        JLabel supplierDistanceLabel = new JLabel("Distance");

        JButton supplierAdd = new JButton("Add");
        JButton supplierDelete = new JButton("Delete");


        JScrollPane textBoxSupplier = new JScrollPane(suppliersTable);


        JPanel boxesSeven = new JPanel(new GridLayout(3, 2));
        boxesSeven.add(supplierNameLabel);
        boxesSeven.add(supplierName);
        boxesSeven.add(supplierPostcodeLabel);
        boxesSeven.add(supplierPostcode);

        JPanel suppliersButtons = new JPanel();
        suppliersButtons.add(supplierAdd, BorderLayout.SOUTH);
        suppliersButtons.add(supplierDelete,BorderLayout.SOUTH);

        JPanel southSeven = new JPanel(new GridLayout(2, 1));
        southSeven.add(boxesSeven);
        southSeven.add(suppliersButtons);


        JPanel supplier = new JPanel();
        supplier.setLayout(new BorderLayout());
        supplier.add(textBoxSupplier, BorderLayout.CENTER);
        supplier.add(southSeven, BorderLayout.SOUTH);

        int supplierRow = 0;

        for (Supplier x : mock.suppliers) {
            try {
                modelSeven.addRow(new Supplier[]{x});
                suppliersTable.setValueAt(x.getPostcode(), supplierRow, 1);
                suppliersTable.setValueAt(x.getDistance(), supplierRow, 2);
                supplierRow++;
            } catch (Exception e) {

            }
        }

        supplierAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean change = true;

                for (Supplier x : mock.getSuppliers()) {


                    if (x.getName().equals(supplierName.getText())) {
                        try {
                            throw new Exception();
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, "Error: The Supplier already exists");
                            change = false;
                        }
                    }
                }


                if (supplierName.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"The Postcode field cannot be empty");
                    change = false;
                }

                if (change){
                    mock.addSupplier(supplierName.getText(), (Postcode) supplierPostcode.getSelectedItem());
                }

                ingredientSupplier.removeAllItems();

                for (Supplier x : mock.suppliers){
                    ingredientSupplier.addItem(x);
                }

                int rowCount = modelSeven.getRowCount();
                for (int i = rowCount; i >= 0; i--) {
                    try {
                        modelSeven.removeRow(i);
                    } catch (Exception c) {

                    }
                }
                int supplierRow = 0;
                for (Supplier x : mock.suppliers) {
                    try {

                        modelSeven.addRow(new Supplier[]{x});

                        suppliersTable.setValueAt(x.getPostcode(), supplierRow, 1);
                        suppliersTable.setValueAt(x.getDistance(), supplierRow, 2);
                        supplierRow++;

                    }

                    catch (Exception e1) {

                    }
                }


            }
        });

        supplierDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = suppliersTable.getSelectedRow();


                try {
                    boolean change = true;
                    String selectedValue = modelSeven.getValueAt(selectedRow, 0).toString();

                    int ingredientRowCount = modelEight.getRowCount();
                    for (int i = 0; i < ingredientRowCount; i++) {
                        if (modelEight.getValueAt(i, 2).toString().equals(selectedValue)) {
                            JOptionPane.showMessageDialog(null, "Error: This Supplier cannot be deleted as it is being used to order an Ingredient");
                            change = false;
                        }
                    }


                    if (change) {
                        try {
                            for (Supplier x : mock.getSuppliers()) {

                                if (x.getName().equals(selectedValue)) {
                                    try {

                                        mock.removeSupplier(x);
                                        mock.suppliers.remove(x);
                                        modelSeven.removeRow(selectedRow);

                                    }

                                    catch (Exception e1) {

                                    }
                                }
                            }
                        }

                        catch (Exception e1) {

                        }

                    }
                }catch (ArrayIndexOutOfBoundsException e1){
                    JOptionPane.showMessageDialog(null,"Error: Please select a row to delete");
                }
            }
        });


        //                                                                          SUPPLIERS FINISHED

        //Tab for postcode                                                                POSTCODE START


        JTable postTable = new JTable();

        Object[] columnsTwo = {"Postcode", "Distance", "LatLong"};
        DefaultTableModel modelTwo = new DefaultTableModel();
        modelTwo.setColumnIdentifiers(columnsTwo);

        postTable.setModel(modelTwo);

        JTextField postCodeField = new JTextField();

        JLabel postCodeFieldLabel = new JLabel("Postcode");


        JButton postAdd = new JButton("Add");
        JButton postDelete = new JButton("Delete");


        JScrollPane textBoxPostcode = new JScrollPane(postTable);


        JPanel boxesTwo = new JPanel(new GridLayout(1, 2));
        boxesTwo.add(postCodeFieldLabel);
        boxesTwo.add(postCodeField);

        JPanel postButtons = new JPanel();
        postButtons.add(postAdd);
        postButtons.add(postDelete);

        JPanel southTwo = new JPanel();
        southTwo.setLayout(new GridLayout(2, 1)); // REMOVE TO MAKE TEXT BOXES SMALLER
        southTwo.add(boxesTwo);
        southTwo.add(postButtons);

        JPanel postcode = new JPanel();
        postcode.setLayout(new BorderLayout());
        postcode.add(textBoxPostcode, BorderLayout.CENTER);
        postcode.add(southTwo, BorderLayout.SOUTH);

        int postRow = 0;
        for (Postcode x : mock.postcodes) {
            try {
                modelTwo.addRow(new Postcode[]{x});
                modelTwo.setValueAt(x.getDistance(), postRow, 1);
                modelTwo.setValueAt(x.getLatLong(), postRow, 2);
                postRow++;
            } catch (Exception e) {

            }

        }



        postAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                boolean change = true;

                for (Postcode x : mock.getPostcodes()) {


                    if (x.getName().equals(postCodeField.getText())) {
                        try {
                            throw new Exception();
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(null, "Error: The Postcode already exists");
                            change = false;
                        }
                    }
                }


                if (postCodeField.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"The Postcode field cannot be empty");
                    change = false;
                }

                if (change){
                    mock.addPostcode(postCodeField.getText());
                }



                supplierPostcode.removeAllItems();

                for (Postcode x : mock.postcodes){
                    supplierPostcode.addItem(x);
                }


                int rowCount = modelTwo.getRowCount();
                for (int i = rowCount - 1; i >= 0; i--) {
                    modelTwo.removeRow(i);
                }

                int postRow = 0;
                for (Postcode x : mock.postcodes) {
                    try {
                        modelTwo.addRow(new Postcode[]{x});
                        modelTwo.setValueAt(x.getDistance(), postRow, 1);
                        modelTwo.setValueAt(x.getLatLong(), postRow, 2);
                        postRow++;
                    } catch (Exception e1) {

                    }

                }
            }
        });

        postDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = postTable.getSelectedRow();

                String selectedValue = modelTwo.getValueAt(selectedRow, 0).toString();

                boolean change = true;


                int supplierRowCount = modelSeven.getRowCount();
                for(int i =0; i<supplierRowCount; i++){
                    if (modelSeven.getValueAt(i,1).toString().equals(selectedValue)){
                        JOptionPane.showMessageDialog(null,"Error: This Postcode cannot be deleted as it is being used by a Supplier");
                        change = false;
                    }
                }


                if (change) {
                    try {
                        for (Postcode x : mock.getPostcodes()) {

                            if (x.getName().equals(selectedValue)) {
                                try {
                                    mock.removePostcode(x);
                                    mock.postcodes.remove(x);
                                    modelTwo.removeRow(selectedRow);
                                } catch (UnableToDeleteException e1) {

                                }
                            }
                        }
                    } catch (Exception e1) {

                    }
                }

            }
        });


        //                                                                          POSTCODE FINISHED


        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Orders", orders);

        tabs.add("Users", users);

        tabs.add("Suppliers", supplier);

        tabs.add("Ingredients", ingredients)
        ;
        tabs.add("Dishes", dishes);

        tabs.add("Postcode", postcode);

        tabs.add("Drone", drone);

        tabs.add("Staff", staff);
        
        this.add(tabs);


    }

    /**
     * Start the timer which updates the user interface based on the given interval to update all panels
     */
    public void startTimer() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        int timeInterval = 5;

        scheduler.scheduleAtFixedRate(() -> refreshAll(), 0, timeInterval, TimeUnit.SECONDS);
    }

    /**
     * Refresh all parts of the server application based on receiving new data, calling the server afresh
     */
    public void refreshAll() {

    }

    @Override
    /**
     * Respond to the model being updated by refreshing all data displays
     */
    public void updated(UpdateEvent updateEvent) {
        refreshAll();
    }

}
