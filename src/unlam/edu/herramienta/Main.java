package unlam.edu.herramienta;

import java.awt.EventQueue;

	public class Main {
	
		public static void main(String[] args) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						HerramientaTesting frame = new HerramientaTesting();
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

}
