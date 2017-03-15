package hanoi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import hanoi.HanoiGame.MovementCompletedCallback;


public class HanoiGuiFactory {
	private static final int VERTICAL_BAR_WIDTH = 20;
	private static final int VERTICAL_BAR_HEIGHT = 300;
	private static final int VERTICAL_BAR_STEP = 300;
	private static final int VERTICAL_BAR_Y = 100;
	private static final int VERTICAL_BAR_X = 100;
	
	private static final int MAX_DISK_WIDTH = 200;
	private static final int DISK_HEIGHT = 20;
	private static final int DISK_WIDTH_STEP = 20;
	
	private static final int TABLE_HEIGHT = 250;
	
	
	public static JLabel getVerticalBar(int barIndex){
		JLabel label = new JLabel(String.valueOf(barIndex), SwingConstants.HORIZONTAL);
		label.setVerticalAlignment(JLabel.TOP);
		label.setSize(VERTICAL_BAR_WIDTH, VERTICAL_BAR_HEIGHT);
		label.setLocation(VERTICAL_BAR_X + barIndex * VERTICAL_BAR_STEP, VERTICAL_BAR_Y);
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Serif", Font.BOLD, 20));
		label.setOpaque(true);

		return label;
	}
	
	public static JScrollPane getScrollPane(JTable table){
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setSize(VERTICAL_BAR_STEP * 2, TABLE_HEIGHT);
        scrollPane.setLocation(VERTICAL_BAR_X, VERTICAL_BAR_HEIGHT + VERTICAL_BAR_Y + VERTICAL_BAR_Y);
        return scrollPane;
        
	}
	
	public static JLabel getHorizontalDisk(int globalDiskIndex, int totalDisks){
		JLabel label = new JLabel(String.valueOf(globalDiskIndex),  SwingConstants.CENTER);
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		int diskWidth = MAX_DISK_WIDTH - (totalDisks - globalDiskIndex - 1) * DISK_WIDTH_STEP;
		label.setSize(diskWidth, DISK_HEIGHT);
		label.setBackground(Color.YELLOW);
		label.setLocation(calculateDiskLocation(label, totalDisks - 1 - globalDiskIndex, 0));

		label.setOpaque(true);
		return label;
	}
	
	
	public static JLabel getNotificationLabel(){
		JLabel label = new JLabel("",  SwingConstants.CENTER);
		label.setFont(new Font("Serif", Font.BOLD, 25));
		label.setForeground(Color.RED);
		label.setSize(VERTICAL_BAR_STEP * 2, DISK_HEIGHT * 3);
		label.setLocation(VERTICAL_BAR_X, VERTICAL_BAR_HEIGHT + VERTICAL_BAR_Y + VERTICAL_BAR_Y/2);
		return label;
	}
	
	public static Point calculateDiskLocation(JLabel disk, int diskIndexOnDestinationBar, int destinationBarIndex){
		
		
		int diskWidth = disk.getWidth();

		int x = VERTICAL_BAR_X + VERTICAL_BAR_WIDTH/2 + (VERTICAL_BAR_STEP * destinationBarIndex)  - diskWidth/2;
		int y = VERTICAL_BAR_Y + VERTICAL_BAR_HEIGHT - DISK_HEIGHT * (diskIndexOnDestinationBar + 1);
		Point location = new Point(x, y); //x,y
		return location;
		
	}
	
public static void animateMovement(JLabel label, Point destinationLocation, MovementCompletedCallback callback){
		
		
		Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	int sourceX = (int) label.getLocation().getX();
        		int sourceY = (int) label.getLocation().getY();
        		int destinationX = (int) destinationLocation.getX();
        		int destinationY = (int) destinationLocation.getY();
            	
                if (sourceX == destinationX && sourceY == destinationY) {
                    ((Timer)e.getSource()).stop();
                    label.setBackground(Color.CYAN);
                    if(callback.isPerformAllSteps()){
                    	callback.performNextStepCallback();
                    }else{
                    	callback.movementCompletedCallback();
                    }
                    
                }
                
                int deltaX = 0;
                if(sourceX != destinationX){
                	deltaX = sourceX < destinationX ? 1 : -1;
                	
                }
                
                int deltaY = 0;
                if(sourceY != destinationY){
                	deltaY = sourceY < destinationY ? 1 : -1;
                }
                
                label.setLocation(sourceX + deltaX, sourceY + deltaY);
                
                Color background = label.getBackground();
                if(background == Color.YELLOW){
                	label.setBackground(Color.CYAN);
                }else{
                	label.setBackground(Color.YELLOW);
                }
            }
        });
		
		timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
	}

}
