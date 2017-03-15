package hanoi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class HanoiGame {
	private JLabel[] disks;
	private JLabel[] verticalBars;
	private JLabel notificationLabel;
	private int[][] disksStackModel; //FILO
	private JFrame frame;
	private JButton nextStepButton;
	private JButton allStepsButton;
	private JTable table;
	
	private final static int MAX_PERMITTED_STEPS = 100;
	private int[][] steps = new int[MAX_PERMITTED_STEPS][2];
	private int nextStep = 0;
	private int totalSteps = 0;
	
	public HanoiGame(int totalDisks){
		this(totalDisks, 3);
		for(int i = 0; i < MAX_PERMITTED_STEPS; i++){
			steps[i][0] = -1;
			steps[i][1] = -1;
		}
	}
	
	
	private void enableNextStepButton(){
		if(nextStep < totalSteps){
			nextStepButton.setEnabled(true);
			allStepsButton.setEnabled(true);
		}
		
	}
	
	private void disableNextStepButton(){
		nextStepButton.setEnabled(false);
		allStepsButton.setEnabled(false);
	}
	
	
	public void addStep(int sourceBarIndex, int destinationBarIndex){
		try {
			addNextStep(sourceBarIndex, destinationBarIndex);
		} catch (Exception e) {
			System.err.print(e.getMessage());
		}
	}
	
	private void addNextStep(int sourceBarIndex, int destinationBarIndex) throws Exception{
		
		if(totalSteps + 1 > MAX_PERMITTED_STEPS){
			throw new Exception("Maximum number of permitted steps: " + MAX_PERMITTED_STEPS + " reached");
		}else{
			steps[totalSteps][0] = sourceBarIndex;
			steps[totalSteps][1] = destinationBarIndex;
			
			updateTableModel(totalSteps, sourceBarIndex, destinationBarIndex);
			enableNextStepButton();
			totalSteps++;
		}
	}
	
	private void updateTableModel(int currentStep, int sourceBarIndex, int destinationBarIndex){
		DefaultTableModel model = (DefaultTableModel) table.getModel();
	    Object[] row = { null, totalSteps, sourceBarIndex, destinationBarIndex };
	    model.addRow(row);
	}
	
	private JButton createNextStepButton(){
		JButton button = new JButton("Next step");
		button.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  disableNextStepButton();
				  performNextStep(false);
			  } 
		});
		button.setEnabled(false);
		button.setBackground(Color.BLUE);
		button.setLocation(10, 10);
		button.setSize(100, 50);
		return button;
	}
	
	private JButton createAllStepButton(){
		JButton button = new JButton("All steps");
		button.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  disableNextStepButton();
				  performNextStep(true);
			  } 
		});
		button.setEnabled(false);
		button.setBackground(Color.BLUE);
		button.setLocation(150, 10);
		button.setSize(100, 50);
		return button;
	}
	
	private void performNextStep(boolean performAllSteps){
		if(nextStep < totalSteps){
			try {
				moveDiskInternal(steps[nextStep][0], steps[nextStep][1], performAllSteps);
				highlightCurrentStepInTable();
				nextStep++;
			} catch (Exception e) {
				String error = e.getMessage();
				showErrorMessage(error);
				System.err.print(error);
			}
			
		}
		
	}
	
	private void highlightCurrentStepInTable(){
		DefaultTableModel model = (DefaultTableModel)table.getModel();

		if(nextStep > 0){
			model.setValueAt("Previous Step", nextStep - 1, 0);
		}
		
		model.setValueAt("Last step", nextStep, 0);
	}
	
	private JTable createEmptyJTable(){        
        String[] columnNames = {"Current Step", "Step ID", "From Bar", "To Bar"};
        int numRows = 0;
        DefaultTableModel model = new DefaultTableModel(numRows, columnNames.length) ;
        model.setColumnIdentifiers(columnNames);
        JTable table  = new JTable(model) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) {                
                    return false;               
            };
            
        };
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setFillsViewportHeight(true);
        return table;

	}
	
	private HanoiGame(int totalDisks, int totalVerticalBars){
        disksStackModel = new int[totalVerticalBars][totalDisks];

		
        for(int i = 0; i < totalVerticalBars; i++){
        	for(int j = 0; j < totalDisks; j ++){
        		disksStackModel[i][j] = -1;	//-1 means that this is empty cell
        	}
        }
        
		frame = new JFrame("Absolute Layout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setOpaque(true);
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(null);

        
        disks = new JLabel[totalDisks];
        for(int i = 0; i < totalDisks; i++){
        	JLabel disk = HanoiGuiFactory.getHorizontalDisk(i, totalDisks);
        	contentPane.add(disk);
        	disks[i] = disk;
        	disksStackModel[0][i] = totalDisks - 1 - i;
        }
        
        verticalBars = new JLabel[totalVerticalBars];
        for(int i = 0; i < totalVerticalBars; i++){
            JLabel verticalBar = HanoiGuiFactory.getVerticalBar(i);
            contentPane.add(verticalBar);
            verticalBars[i] = verticalBar;
        }
        
        nextStepButton = createNextStepButton();
        contentPane.add(nextStepButton);
        
        allStepsButton = createAllStepButton();
        contentPane.add(allStepsButton);
        
        table = createEmptyJTable();
        JScrollPane scrollPane = HanoiGuiFactory.getScrollPane(table);
        contentPane.add(scrollPane);
        
        notificationLabel = HanoiGuiFactory.getNotificationLabel();
        contentPane.add(notificationLabel);

        frame.setContentPane(contentPane);
        frame.setSize(1000, 1000);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);        
        
	}
	
	

	
	private void showErrorMessage(String error){
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setValueAt("Last step - ERROR!", nextStep, 0);
		notificationLabel.setText(error);
		
		Font labelFont = notificationLabel.getFont();
		int stringWidth = notificationLabel.getFontMetrics(labelFont).stringWidth(error);
		int componentWidth = notificationLabel.getWidth();
		
		// Find out how much the font can grow in width.
		double widthRatio = (double)componentWidth / (double)stringWidth;

		int newFontSize = (int)(labelFont.getSize() * widthRatio);
		int componentHeight = notificationLabel.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		int fontSizeToUse = Math.min(newFontSize, componentHeight);

		// Set the label's font size to the newly determined size.
		notificationLabel.setFont(new Font(labelFont.getName(), Font.BOLD, fontSizeToUse));
	}
	
	private void moveDiskInternal(int sourceBarIndex, int destinationBarIndex, boolean performAllSteps) throws Exception{
		int globalDiskIndex = -1;
		JLabel disk = null;
		int diskIndexInSourceModel = -1;
		int topDiskIndexInDestinationModel = -1;
		
		//if there aren't disks in source bar
		if(disksStackModel[sourceBarIndex][0] == -1){
			throw new Exception("There aren't disks in " + sourceBarIndex + " bar");
		}
		
		
		//looking for disk which we need to move from sourceBar in model
		for(int i = disksStackModel[sourceBarIndex].length -1 ; i >= 0 ; i--){
			if(disksStackModel[sourceBarIndex][i] != -1){
				globalDiskIndex = disksStackModel[sourceBarIndex][i];
				disk = disks[globalDiskIndex];
				diskIndexInSourceModel = i;
				break;
			}
		}
		
		
		//calculate how many disks in destinationBar, by looking first cell without disk (with -1 in it)
		for(int i = 0 ; i < disksStackModel[destinationBarIndex].length; i++){
			if(disksStackModel[destinationBarIndex][i] > -1){
				topDiskIndexInDestinationModel = i;		  
			}
		}
		
		//if there're disks on destinationBar and  top disk on destination bar is smaller then disk we want to place on it
		if(topDiskIndexInDestinationModel >= 0 && disksStackModel[destinationBarIndex][topDiskIndexInDestinationModel] < globalDiskIndex){
			int destinationUpperDiskIndex = disksStackModel[destinationBarIndex][topDiskIndexInDestinationModel];
			throw new Exception("You're trying to put bigger disk " + globalDiskIndex +  " from bar " + sourceBarIndex +
									" on smaller disk " + destinationUpperDiskIndex + " on bar " + destinationBarIndex);
		}
		
		
		
		moveDisk(disk, diskIndexInSourceModel, destinationBarIndex, topDiskIndexInDestinationModel, performAllSteps);
		updateDisksModel(globalDiskIndex, diskIndexInSourceModel, sourceBarIndex, topDiskIndexInDestinationModel, destinationBarIndex);
	}
	
	public abstract class MovementCompletedCallback{
		boolean performAllSteps;
		public MovementCompletedCallback(boolean performAllSteps){
			this.performAllSteps = performAllSteps;
		}
		
		public boolean isPerformAllSteps(){
			return performAllSteps;
		}
		
		abstract void movementCompletedCallback();
        abstract void performNextStepCallback();
    }
	
	private void moveDisk(JLabel disk, int diskIndexInSourceModel, int destinationBarIndex, int topDiskIndexInDestinationModel, boolean performAllSteps){		
		Point to = HanoiGuiFactory.calculateDiskLocation(disk, topDiskIndexInDestinationModel + 1, destinationBarIndex);
		HanoiGuiFactory.animateMovement(disk, to, new MovementCompletedCallback(performAllSteps) {
			
			@Override
			public void movementCompletedCallback() {
				enableNextStepButton();
				
			}

			@Override
			public void performNextStepCallback() {
				performNextStep(true);	
			}
		});
	}
	
	private void updateDisksModel(int globalDiskIndex, int diskIndexInSourceModel, int sourceBarIndex, int topDiskIndexInDestinationModel, int destinationBarIndex){
		disksStackModel[sourceBarIndex][diskIndexInSourceModel] = -1;
		disksStackModel[destinationBarIndex][topDiskIndexInDestinationModel + 1] = globalDiskIndex;

	}
	
 }
