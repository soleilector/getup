import java.util.HashMap;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
public class UI_Elements {
	public final static int HEALTH_BAR_MAX_WIDTH = 300;
	public static HashMap<String,Component> elements = new HashMap<>() {{
		put("healthBarFG",
				new JPanel() {{
					setPreferredSize(new Dimension(HEALTH_BAR_MAX_WIDTH, 30));
					//setLayout(centerLayout);
					setBackground(Color.GREEN);
				}}
			);
		put("healthBarPanel",
				new JPanel() {{
					setPreferredSize(new Dimension(300, 30));
					//setLayout(leftLayout);
					setBackground(Color.gray);
					//add(healthBarFG);
				}}
			);
		put("healthPanel",
				new JPanel() {{
					//setPreferredSize(new Dimension(548, statusBarYSize));
					//setBackground(mainColor);
					//setLayout(statusBarLayout);
					//add(healthBarLabel);
					//add(healthBarPanel);
				}}
			);
	}};
	
	public static void validate() {
		//elements.get("healthPanel").add(elements.get("healthBarPanel"));
	}
}
