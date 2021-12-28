import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

class WeekdayLabel extends JLabel implements Serializable {
    JLabel[] weekday = new JLabel[7];
    JButton[] days = new JButton[42];

    WeekdayLabel() {
        for (int i = 0; i < 7; i++) {
            weekday[i] = new JLabel("", JLabel.CENTER);
        }
        for (int i = 0; i < 42; i++) {
            days[i] = new JButton();
        }
        weekday[0].setText("周日");
        weekday[1].setText("周一");
        weekday[2].setText("周二");
        weekday[3].setText("周三");
        weekday[4].setText("周四");
        weekday[5].setText("周五");
        weekday[6].setText("周六");
    }
}

class Initial implements Serializable {
    int firstDay;
    int maxDay;
    int today;

    Initial() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        today = calendar.get(Calendar.DAY_OF_MONTH);
        maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;

    }
}

public class EventCalendar extends JFrame implements Serializable {
    static JLabel date = new JLabel("未知日期");
    JLabel showDate = new JLabel("");
    WeekdayLabel weekdays = new WeekdayLabel();
    public JButton last, next;
    public JFrame MyCalendar;
    public JPanel top, middle, bottom, itemManage, itemList;
    public JButton addItem = new JButton();
    public JButton clearItem = new JButton();
    public JButton save = new JButton();
    public JButton returnMain = new JButton();
    public JButton upLoad = new JButton();
    Initial initial = new Initial();
    static ArrayList<String> itemsOfToday = new ArrayList<>();
    static ArrayList<String> timeOfItem = new ArrayList<>();
    Client client;

    int i = 0;

    Vector<ArrayList<JPanel>> items = new Vector<ArrayList<JPanel>>();

    void creat() {

        MyCalendar = new JFrame("Calendar");
        MyCalendar.setSize(500, 500);
        last = new JButton("上月");
        next = new JButton("下月");
        last.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) + --i);
                if (i == 0) {
                    showDate.setText("");
                    calendar.setTime(new Date());
                    int today = calendar.get(Calendar.DAY_OF_MONTH);
                    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    int firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    for (JButton j : weekdays.days) {
                        j.setText("");
                    }
                    for (int i = 0; i < maxDay; i++) {
                        weekdays.days[firstDay + i].setText((i + 1) + "");
                    }
                    weekdays.days[firstDay + today - 1].setText("*" + today);
                } else {
                    if (i > 0)
                        showDate.setText("后" + i + "月");
                    else
                        showDate.setText("前" + -i + "月");
                    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    int firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    for (JButton j : weekdays.days) {
                        j.setText("");
                    }
                    for (int i = 0; i < maxDay; i++) {
                        weekdays.days[firstDay + i].setText((i + 1) + "");
                    }
                }

            }
        });
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) + ++i);
                if (i == 0) {
                    showDate.setText("");
                    calendar.setTime(new Date());
                    int today = calendar.get(Calendar.DAY_OF_MONTH);
                    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    int firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    for (JButton j : weekdays.days) {
                        j.setText("");
                    }
                    for (int i = 0; i < maxDay; i++) {
                        weekdays.days[firstDay + i].setText((i + 1) + "");
                    }
                    weekdays.days[firstDay + today - 1].setText("*" + today);
                } else {
                    if (i > 0)
                        showDate.setText("后" + i + "月");
                    else
                        showDate.setText("前" + -i + "月");
                    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    int firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    for (JButton j : weekdays.days) {
                        j.setText("");
                    }
                    for (int i = 0; i < maxDay; i++) {
                        weekdays.days[firstDay + i].setText((i + 1) + "");
                    }
                }
            }
        });
        top = new JPanel();
        top.add(date);
        top.add(last);
        top.add(next);
        top.add(showDate);
        top.setLayout(new FlowLayout());
        middle = new JPanel();
        for (int i = 0; i < 7; i++) {
            middle.add(weekdays.weekday[i]);
        }
        for (int i = 0; i < 42; i++) {
            middle.add(weekdays.days[i]);

        }
        middle.setLayout(new GridLayout(7, 7));
        bottom = new JPanel();
        addItem.setText("添加日程");
        clearItem.setText("清空日程");
        clearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 42; i++) {
                    items.get(i).clear();
                }
                itemList.removeAll();
                itemList.revalidate();
                itemsOfToday.clear();
                timeOfItem.clear();


            }
        });
        returnMain.setText("返回");
        returnMain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itemList.removeAll();
                itemList.revalidate();
            }
        });
        save.setText("保存");
        upLoad.setText("上传");

        itemManage = new JPanel();
        itemManage.setLayout(new FlowLayout());
        itemManage.add(addItem);
        itemManage.add(clearItem);
        itemManage.add(returnMain);
        itemManage.add(save);
        itemManage.add(upLoad);
        itemList = new JPanel();
        itemList.setLayout(new GridLayout(10, 1));
        bottom.setLayout(new BorderLayout());
        bottom.add(BorderLayout.NORTH, itemManage);
        bottom.add(BorderLayout.SOUTH, itemList);
        MyCalendar.setLayout(new BorderLayout());
        MyCalendar.add(BorderLayout.NORTH, top);
        MyCalendar.add(BorderLayout.CENTER, middle);
        MyCalendar.add(BorderLayout.SOUTH, bottom);
        MyCalendar.setVisible(true);
        MyCalendar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    EventCalendar() {
        client = new Client();

        for (int i = 0; i < 42; i++) {
            items.add(new ArrayList<JPanel>());
        }
        for (int i = 0; i < 42; i++) {
            int finalI = i;
            weekdays.days[finalI].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (addItem.getActionListeners().length > 0) {
                        for (ActionListener g : addItem.getActionListeners()) {
                            addItem.removeActionListener(g);
                        }
                    }
                    addItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String time = JOptionPane.showInputDialog("日程时间（例：10:05）");
                            String item = JOptionPane.showInputDialog("日程");
                            if (finalI == initial.firstDay + initial.today - 1) {
                                itemsOfToday.add(item);
                                timeOfItem.add(time);
                            }
                            JPanel itemPanel = new JPanel();
                            itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                            itemPanel.add(new JLabel(time + "  " + item, JLabel.LEFT));
                            items.get(finalI).add(itemPanel);
                            itemList.add(items.get(finalI).get(items.get(finalI).size() - 1));
                            itemList.revalidate();
                        }
                    });

                    itemList.removeAll();


                    for (int n = 0; n < items.get(finalI).size(); n++) {
                        itemList.add(items.get(finalI).get(n));
                    }
                    itemList.revalidate();
                }
            });
        }
        LocalDate date = LocalDate.now();
        this.date.setText("今日:" + date.toString());

        for (int i = 0; i < initial.maxDay; i++) {
            weekdays.days[initial.firstDay + i].setText((i + 1) + "");
        }
        weekdays.days[initial.firstDay + initial.today - 1].setText("*" + initial.today);


    }

    static class Remind implements Runnable, Serializable {
        DateFormat df = new SimpleDateFormat("HH:mm");


        @Override
        public void run() {
            while (true) {

                Date date = new Date();
                String d1 = df.format(date);


                if (!itemsOfToday.isEmpty()) {

                    for (int i = 0; i < itemsOfToday.size(); i++) {
                        if (timeOfItem.get(i).equals(d1)) {
                            JOptionPane.showMessageDialog(null, itemsOfToday, "日程提醒", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Client implements Serializable {
        Socket socket;
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        EventCalendar ecal;

        Client() {
            ecal = null;
            socket = null;
            objectOutputStream = null;
            objectInputStream = null;
        }


        void send() throws IOException {
            OutputStream os = null;
            ecal = new EventCalendar();

            File file = new File("MYCalendar.cal");
            if (file.exists()) {

                try (ObjectInputStream in =
                             new ObjectInputStream(
                                     new BufferedInputStream(

                                             new FileInputStream("MYCalendar.cal")
                                     )
                             )) {

                    ecal = (EventCalendar) in.readObject();
                    socket = new Socket("127.0.0.1", 8888);
                    os = socket.getOutputStream();
                    objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(os));
                    objectOutputStream.writeObject(ecal);
                    objectOutputStream.close();
                    os.close();


                } catch (IOException | ClassNotFoundException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        EventCalendar receive() throws IOException, ClassNotFoundException {
            InputStream is = null;
            try {
                ecal = new EventCalendar();
                socket = new Socket("127.0.0.1", 8888);
            /*OutputStream os = socket.getOutputStream();
            ObjectOutputStream objo = new ObjectOutputStream(new BufferedOutputStream(os));
            objo.writeObject(ecal);*/


                is = socket.getInputStream();
                objectInputStream = new ObjectInputStream(new BufferedInputStream(is));
                ecal = new EventCalendar();
                ecal = (EventCalendar) objectInputStream.readObject();
                socket.shutdownInput();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {

                objectInputStream.close();
                is.close();
                socket.close();
            }
            return ecal;
        }
    }


    public static void main(String[] args) {
        EventCalendar my = new EventCalendar();
        my.upLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    my.client.send();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
                                       EventCalendar objIn = new EventCalendar();

                                       public void run() {
                                           File file = new File("MYCalendar.cal");
                                           if (file.exists()) {

                                               try (ObjectInputStream in =
                                                            new ObjectInputStream(
                                                                    new BufferedInputStream(

                                                                            new FileInputStream("MYCalendar.cal")
                                                                    )
                                                            )) {

                                                   objIn = (EventCalendar) in.readObject();
                                               } catch (FileNotFoundException e) {
                                                   e.printStackTrace();
                                               } catch (IOException e) {
                                                   e.printStackTrace();
                                               } catch (ClassNotFoundException e) {
                                                   e.printStackTrace();
                                               }
                                               for (int i = 0; i < 42; i++) {
                                                   my.items.get(i).addAll(objIn.items.get(i));
                                               }


                                           }
                   /*else{
                       try {
                           objIn = my.client.receive();
                       } catch (IOException e) {
                           e.printStackTrace();
                       } catch (ClassNotFoundException e) {
                           e.printStackTrace();
                       }
                       for(int i = 0;i<42;i++){
                           my.items.get(i).addAll(objIn.items.get(i));
                       }
                   }*/


                                           my.creat();
                                           my.save.addActionListener(new ActionListener() {
                                               @Override
                                               public void actionPerformed(ActionEvent e) {
                                                   try (ObjectOutputStream out =
                                                                new ObjectOutputStream(
                                                                        new BufferedOutputStream(
                                                                                new FileOutputStream("MYCalendar.cal")))) {
                                                       File file = new File("MYCalendar.cal");
                                                       if (file.exists()) {
                                                           file.delete();
                                                       }
                                                       out.writeObject(my);
                                                       out.flush();
                                                       out.close();

                                                   } catch (IOException ex) {
                                                       ex.printStackTrace();
                                                   }


                                               }
                                           });


                                       }
                                   }
        );
        Thread re = new Thread(new Remind());
        re.start();
    }
}