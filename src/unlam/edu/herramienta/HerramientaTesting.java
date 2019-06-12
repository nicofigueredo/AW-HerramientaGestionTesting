package unlam.edu.herramienta;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import unlam.edu.ayuda.TextAreaUpdater;
import unlam.edu.entidades.Clase;
import unlam.edu.entidades.Metodo;
import unlam.edu.lector.LectorJavaParserAvanzado;
import unlam.edu.metricas.Metrica;
import unlam.edu.metricas.ResultadoMetrica;
import unlam.edu.metricas.impl.CantidadComentarios;
import unlam.edu.metricas.impl.CantidadLineas;
import unlam.edu.metricas.impl.ComplejidadCiclomatica;
import unlam.edu.metricas.impl.FanIn;
import unlam.edu.metricas.impl.FanOut;
import unlam.edu.metricas.impl.Halstead;

public class HerramientaTesting extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFileChooser fileChooser;
	private JList<String> filesList;
	private JList<String> classesList;
	private JList<String> methodsList;
	private TextArea codeTextPane;
	
	private JLabel lblLineasDeCodigoResult;
	private JLabel lblLineasDeCodigoComentadasResult;
	private JLabel lblPorcentajeDeLineasDeCodigoComentadasResult;
	private JLabel lblComplejidadCiclomaticaResult;
	private JLabel lblFanInResult;
	private JLabel lblFanOutResult;
	private JLabel lblHalsteadLongitudResult;
	private JLabel lblHalsteadVolumenResult;
	
	
	public void initMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnAnalisis = new JMenu("Analisis");
		menuBar.add(mnAnalisis);
		
		JMenuItem mntmElegirCarpeta = new JMenuItem("Elegir Carpeta");
		mntmElegirCarpeta.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mntmElegirCarpeta.addActionListener(cargarArchivos);
		
		mnAnalisis.add(mntmElegirCarpeta);
		
		JMenuItem mntmSalirCtrl = new JMenuItem("Salir");
		mntmSalirCtrl.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnAnalisis.add(mntmSalirCtrl);
	}
	
	public void initMetricLabels() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new LineBorder(new Color(128, 128, 128)));
		panel.setBounds(553, 36, 258, 362);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblLineasDeCodigoTotales = new JLabel("Lineas de codigo totales:");
		lblLineasDeCodigoTotales.setHorizontalAlignment(SwingConstants.CENTER);
		lblLineasDeCodigoTotales.setBounds(10, 11, 238, 14);
		panel.add(lblLineasDeCodigoTotales);
		
		JLabel lblLineasDeCodigoComentadas = new JLabel("Lineas de codigo comentadas:");
		lblLineasDeCodigoComentadas.setHorizontalAlignment(SwingConstants.CENTER);
		lblLineasDeCodigoComentadas.setBounds(10, 51, 238, 14);
		panel.add(lblLineasDeCodigoComentadas);
		
		JLabel lblPorcentajeDeLinea = new JLabel("Porcentaje de lineas de codigo comentadas:");
		lblPorcentajeDeLinea.setHorizontalAlignment(SwingConstants.CENTER);
		lblPorcentajeDeLinea.setBounds(10, 91, 238, 14);
		panel.add(lblPorcentajeDeLinea);
		
		JLabel lblComplejidadCiclomatica = new JLabel("Complejidad ciclomatica:");
		lblComplejidadCiclomatica.setHorizontalAlignment(SwingConstants.CENTER);
		lblComplejidadCiclomatica.setBounds(10, 131, 238, 14);
		panel.add(lblComplejidadCiclomatica);
		
		JLabel lblFanIn = new JLabel("Fan in:");
		lblFanIn.setHorizontalAlignment(SwingConstants.CENTER);
		lblFanIn.setBounds(10, 171, 238, 14);
		panel.add(lblFanIn);
		
		JLabel lblFanOut = new JLabel("Fan out:");
		lblFanOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblFanOut.setBounds(10, 211, 238, 14);
		panel.add(lblFanOut);
		
		JLabel lblHalsteadLongitud = new JLabel("Halstead longitud:");
		lblHalsteadLongitud.setHorizontalAlignment(SwingConstants.CENTER);
		lblHalsteadLongitud.setBounds(10, 251, 238, 14);
		panel.add(lblHalsteadLongitud);
		
		JLabel lblHalsteadVolumen = new JLabel("Halstead volumen:");
		lblHalsteadVolumen.setHorizontalAlignment(SwingConstants.CENTER);
		lblHalsteadVolumen.setBounds(10, 291, 238, 14);
		panel.add(lblHalsteadVolumen);
		
		lblLineasDeCodigoResult = new JLabel("");
		lblLineasDeCodigoResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblLineasDeCodigoResult.setBounds(10, 31, 238, 14);
		panel.add(lblLineasDeCodigoResult);
		
		lblLineasDeCodigoComentadasResult = new JLabel("");
		lblLineasDeCodigoComentadasResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblLineasDeCodigoComentadasResult.setBounds(10, 71, 238, 14);
		panel.add(lblLineasDeCodigoComentadasResult);
		
		lblPorcentajeDeLineasDeCodigoComentadasResult = new JLabel("");
		lblPorcentajeDeLineasDeCodigoComentadasResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblPorcentajeDeLineasDeCodigoComentadasResult.setBounds(10, 111, 238, 14);
		panel.add(lblPorcentajeDeLineasDeCodigoComentadasResult);
		
		lblComplejidadCiclomaticaResult = new JLabel("");
		lblComplejidadCiclomaticaResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblComplejidadCiclomaticaResult.setBounds(10, 151, 238, 14);
		panel.add(lblComplejidadCiclomaticaResult);
		
		lblFanInResult = new JLabel("");
		lblFanInResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblFanInResult.setBounds(10, 191, 238, 14);
		panel.add(lblFanInResult);
		
		lblFanOutResult = new JLabel("");
		lblFanOutResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblFanOutResult.setBounds(10, 231, 238, 14);
		panel.add(lblFanOutResult);
		
		lblHalsteadLongitudResult = new JLabel("");
		lblHalsteadLongitudResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblHalsteadLongitudResult.setBounds(10, 271, 238, 14);
		panel.add(lblHalsteadLongitudResult);
		
		lblHalsteadVolumenResult = new JLabel("");
		lblHalsteadVolumenResult.setHorizontalAlignment(SwingConstants.CENTER);
		lblHalsteadVolumenResult.setBounds(10, 311, 238, 14);
		panel.add(lblHalsteadVolumenResult);
	}
	
	public HerramientaTesting() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setTitle("Herramienta de Gestion de Testing");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 850, 690);
		setLocationRelativeTo(null);
		
		initMenu();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		initMetricLabels();
		
		JLabel lblNewLabel = new JLabel("Seleccione un archivo de la lista");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setBounds(10, 11, 197, 14);
		contentPane.add(lblNewLabel);
		
		JScrollPane filesScrollPane = new JScrollPane();
		filesScrollPane.setBounds(10, 36, 521, 99);
		contentPane.add(filesScrollPane);
		
		filesList = new JList<>();
		filesList.addMouseListener(cargarClases);
		filesScrollPane.setViewportView(filesList);
		
		JLabel lblSeleccioneUnaClase = new JLabel("Seleccione una clase de la lista");
		lblSeleccioneUnaClase.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeleccioneUnaClase.setForeground(new Color(0, 0, 128));
		lblSeleccioneUnaClase.setBounds(22, 146, 185, 14);
		contentPane.add(lblSeleccioneUnaClase);
		
		JScrollPane classesScrollPane = new JScrollPane();
		classesScrollPane.setBounds(10, 171, 258, 202);
		contentPane.add(classesScrollPane);
		
		classesList = new JList<>();
		classesList.addMouseListener(cargarMetodos);
		classesScrollPane.setViewportView(classesList);
		
		JScrollPane methodsScrollPane = new JScrollPane();
		methodsScrollPane.setBounds(278, 171, 253, 202);
		contentPane.add(methodsScrollPane);
		
		methodsList  = new JList<>();
		methodsList.addMouseListener(cargarCodigoYmetricas);
		methodsScrollPane.setViewportView(methodsList);
		
		JLabel lblSeleccioneUnMetodo = new JLabel("Seleccione un metodo de la lista");
		lblSeleccioneUnMetodo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeleccioneUnMetodo.setForeground(new Color(0, 0, 128));
		lblSeleccioneUnMetodo.setBounds(278, 146, 185, 14);
		contentPane.add(lblSeleccioneUnMetodo);
		
		JLabel lblCodigoDelMetodo = new JLabel("Codigo del metodo seleccionado");
		lblCodigoDelMetodo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCodigoDelMetodo.setForeground(new Color(0, 0, 128));
		lblCodigoDelMetodo.setBounds(10, 384, 197, 14);
		contentPane.add(lblCodigoDelMetodo);
		
		JScrollPane codePane = new JScrollPane();
		codePane.setBounds(10, 409, 774, 220);
		contentPane.add(codePane);
		
		codeTextPane = new TextArea();
		codeTextPane.setBackground(Color.WHITE);
		codeTextPane.setEditable(false);
		codePane.setViewportView(codeTextPane);
		
		JLabel lblAnalisisDelMetodo = new JLabel("Analisis del metodo");
		lblAnalisisDelMetodo.setBackground(new Color(240, 240, 240));
		lblAnalisisDelMetodo.setForeground(new Color(0, 0, 128));
		lblAnalisisDelMetodo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAnalisisDelMetodo.setBounds(565, 4, 115, 14);
		contentPane.add(lblAnalisisDelMetodo);
		
		
	}
	
	ActionListener cargarArchivos = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			fileChooser = new JFileChooser();		
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int fileChooserResult = fileChooser.showOpenDialog(null);
			if(fileChooserResult == JFileChooser.APPROVE_OPTION) {
				File carpeta = fileChooser.getSelectedFile();
				File[] archivos = carpeta.listFiles(new FilenameFilter() {
			        @Override
			        public boolean accept(File dir, String name) {
			            return name.toLowerCase().endsWith(".java");
			        }
			    });
		        DefaultListModel<String> filenames = new DefaultListModel<>();			
				
				for(File file: archivos) {				
					filenames.addElement(file.getAbsolutePath());
				}			
				filesList.setModel(filenames);						
			}
		}
	};
	
	private List<Clase> clasesProyecto;
	
	MouseListener cargarClases = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (filesList.getSelectedIndex() == -1)
				return;
			String filename = filesList.getSelectedValue().toString();
			clasesProyecto = new LectorJavaParserAvanzado().leerProyecto(new File(filename));
			DefaultListModel<String> classnames = new DefaultListModel<>();
			for (int indice = 0; indice < clasesProyecto.size(); indice++) {
				classnames.addElement(clasesProyecto.get(indice).getNombre());
            }
			classesList.setModel(classnames);
		}
	};
	
	private List<Metodo> metodosClaseElegida;
	
	MouseListener cargarMetodos = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (classesList.getSelectedIndex() == -1)
				return;
			Integer claseSeleccionada = classesList.getSelectedIndex();
            Clase claseElegida = clasesProyecto.get(claseSeleccionada);
            metodosClaseElegida = claseElegida.getMetodos();
            
			DefaultListModel<String> methodnames = new DefaultListModel<>();
			for (int indice = 0; indice < metodosClaseElegida.size(); indice++) {
				methodnames.addElement(metodosClaseElegida.get(indice).getNombre());
            }
			methodsList.setModel(methodnames);
		}
	};
	
	public List<ResultadoMetrica> calcularMetricas(Metodo metodo) {
        List<ResultadoMetrica> resultados = new ArrayList<ResultadoMetrica>();
        List<Metrica> metricas = new ArrayList<Metrica>();
        metricas.add(new ComplejidadCiclomatica());
        metricas.add(new CantidadLineas());
        metricas.add(new CantidadComentarios());
        metricas.add(new Halstead());
        metricas.add(new FanIn(clasesProyecto));
        metricas.add(new FanOut(clasesProyecto));

        for (Metrica metrica : metricas) {
            metrica.calcular(metodo);
            resultados.add(metrica.obtenerResultado());
        }
        return resultados;
    }

	
    MouseListener cargarCodigoYmetricas = new MouseAdapter() {

        public void mouseClicked(MouseEvent e) {
        	
        	if (methodsList.getSelectedIndex() == -1) 
                return;
            
            Integer metodoSeleccionado = methodsList.getSelectedIndex();            
            Metodo metodoElegido = metodosClaseElegida.get(metodoSeleccionado);
            List<ResultadoMetrica> resultados = calcularMetricas(metodoElegido);

            new Thread(new TextAreaUpdater(codeTextPane, metodoElegido.getCodigo())).start();

            //Aca hay un problema, si cambiamos el orden de resolucion de las metricas esto tambien hay que cambiarlo
            /**
             * Complejidad Ciclomatica
             */
            Integer complejidadCiclomatica = Integer.parseInt(resultados.get(0).getResultado());
            if (complejidadCiclomatica > 10) {
                lblComplejidadCiclomaticaResult.setForeground(Color.RED);
                lblComplejidadCiclomaticaResult.setToolTipText("La complejidad ciclom\u00E1tica supera 10, es recomendable modularizar el m\u00E9todo.");
            } else {
            	lblComplejidadCiclomaticaResult.setForeground(Color.DARK_GRAY);
            	lblComplejidadCiclomaticaResult.setToolTipText(null);
            }
            lblComplejidadCiclomaticaResult.setText(complejidadCiclomatica.toString());

            /**
             * Lineas de codigo, comentarios y porcentaje de comentarios
             */
            lblLineasDeCodigoResult.setText(resultados.get(1).getResultado());
            lblLineasDeCodigoComentadasResult.setText(resultados.get(2).getResultado());

            Double porcentajeComentarios = Integer.parseInt(lblLineasDeCodigoComentadasResult.getText()) * 100.0 / Integer.parseInt(lblLineasDeCodigoResult.getText());

            if (porcentajeComentarios < 15) {
                lblPorcentajeDeLineasDeCodigoComentadasResult.setForeground(Color.RED);
                lblPorcentajeDeLineasDeCodigoComentadasResult.setToolTipText("El porcentaje de comentarios recomendable es del 15%. Agregue m\u00E1s comentarios al m\u00E9todo.");
            } else {
            	lblPorcentajeDeLineasDeCodigoComentadasResult.setForeground(Color.DARK_GRAY);
            	lblPorcentajeDeLineasDeCodigoComentadasResult.setToolTipText(null);
            }
            lblPorcentajeDeLineasDeCodigoComentadasResult.setText(String.format("%.2f", porcentajeComentarios) + "%");

            /**
             * Halstead
             */
            String halstead[] = resultados.get(3).getResultado().split(" ");
            lblHalsteadLongitudResult.setText(halstead[1]);
            lblHalsteadVolumenResult.setText(halstead[3]);

            /**
             * Fan-In y Fan-Out
             */
            lblFanInResult.setText(resultados.get(4).getResultado());
            lblFanOutResult.setText(resultados.get(5).getResultado());

        }
    };	
	
	
}
