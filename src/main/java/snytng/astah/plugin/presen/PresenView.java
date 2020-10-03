package snytng.astah.plugin.presen;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.model.IMindMapDiagram;
import com.change_vision.jude.api.inf.presentation.INodePresentation;
import com.change_vision.jude.api.inf.presentation.IPresentation;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectEvent;
import com.change_vision.jude.api.inf.project.ProjectEventListener;
import com.change_vision.jude.api.inf.ui.IPluginExtraTabView;
import com.change_vision.jude.api.inf.ui.ISelectionListener;
import com.change_vision.jude.api.inf.view.IDiagramEditorSelectionEvent;
import com.change_vision.jude.api.inf.view.IDiagramEditorSelectionListener;
import com.change_vision.jude.api.inf.view.IDiagramViewManager;
import com.change_vision.jude.api.inf.view.IEntitySelectionEvent;
import com.change_vision.jude.api.inf.view.IEntitySelectionListener;

public class PresenView
extends
JPanel
implements
IPluginExtraTabView,
ProjectEventListener,
IEntitySelectionListener,
IDiagramEditorSelectionListener
{
	/**
	 * logger
	 */
	static final Logger logger = Logger.getLogger(PresenView.class.getName());
	static {
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.CONFIG);
		logger.addHandler(consoleHandler);
		logger.setUseParentHandlers(false);
	}

	/**
	 * プロパティファイルの配置場所
	 */
	private static final String VIEW_PROPERTIES = "snytng.astah.plugin.presen.view";

	/**
	 * リソースバンドル
	 */
	private static final ResourceBundle VIEW_BUNDLE = ResourceBundle.getBundle(VIEW_PROPERTIES, Locale.getDefault());

	private String title = "<Presen>";
	private String description = "<This plugin operates the diagram as presentation.>";

	private static final long serialVersionUID = 1L;
	private transient ProjectAccessor projectAccessor = null;
	private transient IDiagramViewManager diagramViewManager = null;

	public PresenView() {
		try {
			projectAccessor = AstahAPI.getAstahAPI().getProjectAccessor();
			diagramViewManager = projectAccessor.getViewManager().getDiagramViewManager();
		} catch (Exception e){
			logger.log(Level.WARNING, e.getMessage(), e);
		}

		initProperties();

		initComponents();
	}

	private void initProperties() {
		try {
			title = VIEW_BUNDLE.getString("pluginExtraTabView.title");
			description = VIEW_BUNDLE.getString("pluginExtraTabView.description");
		}catch(Exception e){
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	private void initComponents() {
		// レイアウトの設定
		setLayout(new GridLayout(1,1));
		add(createButtonsPane());
	}

	private void addDiagramListeners(){
		diagramViewManager.addDiagramEditorSelectionListener(this);
		diagramViewManager.addEntitySelectionListener(this);
	}

	private void removeDiagramListeners(){
		diagramViewManager.removeDiagramEditorSelectionListener(this);
		diagramViewManager.removeEntitySelectionListener(this);
	}

	// 操作ボタン
	JButton addButton    = new JButton("Add");
	JButton insertButton = new JButton("Ins");
	JButton deleteButton = new JButton("Del");
	JButton clearButton = new JButton("Claer");
	JButton saveButton   = new JButton("Save");
	JButton loadButton   = new JButton("Load");
	JButton firstButton  = new JButton("|<");
	JButton prevButton   = new JButton("<");
	JButton numButton    = new JButton("0");
	JButton nextButton   = new JButton(">");
	JButton lastButton   = new JButton(">|");

	// セパレーター
	@SuppressWarnings("serial")
	private JSeparator getSeparator(){
		return new JSeparator(SwingConstants.VERTICAL){
			@Override public Dimension getPreferredSize() {
				return new Dimension(1, 16);
			}
			@Override public Dimension getMaximumSize() {
				return this.getPreferredSize();
			}
		};
	}

	private void activateButtons(){
		setButtonsEnabled(true);
	}
	private void deactivateButtons(){
		setButtonsEnabled(false);
	}
	private void setButtonsEnabled(boolean b){
	}

	private Container createButtonsPane() {

		deactivateButtons();

		//	button mnemonic
		firstButton.setMnemonic(KeyEvent.VK_HOME);
		nextButton.setMnemonic(KeyEvent.VK_RIGHT);
		prevButton.setMnemonic(KeyEvent.VK_LEFT);
		lastButton.setMnemonic(KeyEvent.VK_END);


		// プレゼン操作ボタン
		addButton.addActionListener(e -> {
			addSlide();
			updatePresentationPanel();
			saveSlides();
		});
		insertButton.addActionListener(e -> {
			insertSlide();
			updatePresentationPanel();
			saveSlides();
		});
		deleteButton.addActionListener(e -> {
			deleteSlide();
			updatePresentationPanel();
			saveSlides();
		});
		clearButton.addActionListener(e -> {
			clearSlides();
			updatePresentationPanel();
		});
		saveButton.addActionListener(e -> {
			updatePresentationPanel();
			saveSlides();
		});
		loadButton.addActionListener(e -> {
			loadSlides();
			updatePresentationPanel();
		});
		firstButton.addActionListener(e -> {
			if(presentation.showFirstSlide()) {
				showSlide();
				updatePresentationPanel();
			}
		});
		prevButton.addActionListener(e -> {
			if(presentation.showPrevSlide()) {
				showSlide();
				updatePresentationPanel();
			}
		});
		numButton.addActionListener(e -> {
			showSlide();
			updatePresentationPanel();
		});
		nextButton.addActionListener(e -> {
			if(presentation.showNextSlide()) {
				showSlide();
				updatePresentationPanel();
			}
		});
		lastButton.addActionListener(e -> {
			if(presentation.showLastSlide()) {
				showSlide();
				updatePresentationPanel();
			}
		});


		// 操作パネル
		JPanel operationPanel = new JPanel();
		operationPanel.add(addButton);
		operationPanel.add(insertButton);
		operationPanel.add(deleteButton);
		operationPanel.add(clearButton);
		operationPanel.add(saveButton);
		operationPanel.add(loadButton);
		operationPanel.add(getSeparator());// セパレーター
		operationPanel.add(firstButton);
		operationPanel.add(prevButton);
		operationPanel.add(numButton);
		operationPanel.add(nextButton);
		operationPanel.add(lastButton);


		// パネル配置
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(operationPanel);

		return panel;
	}

	private Presentation presentation = new Presentation();

	private void loadSlides() {
		IDiagram diagram = diagramViewManager.getCurrentDiagram();
		if(diagram == null){
			return;
		}

		String savedString = diagram.getDefinition();
		presentation.loadString(savedString);
	}

	private void saveSlides() {
		IDiagram diagram = diagramViewManager.getCurrentDiagram();
		if(diagram == null){
			return;
		}

		if(presentation.getNumberOfSlides() > 0) {
			try {
				TransactionManager.beginTransaction();
				diagram.setDefinition(presentation.saveString());
				TransactionManager.endTransaction();
			} catch (InvalidEditingException | ClassNotFoundException | IOException e) {
				TransactionManager.abortTransaction();
				e.printStackTrace();
			}
		}
	}

	private void addSlide() {
		double zoomFactor = diagramViewManager.getZoomFactor();
		Point2D originCoord = diagramViewManager.toWorldCoord(0, 0);
		Map<String, String> props = new HashMap<>();
		if(diagramViewManager.getCurrentDiagram() instanceof IMindMapDiagram) {
			try {
				for(IPresentation p : diagramViewManager.getCurrentDiagram().getPresentations()) {
					if(p instanceof INodePresentation) {
						INodePresentation np = (INodePresentation)p;
						boolean npv = isSubTopicVisible(np);
						props.put(np.getID(), Boolean.toString(npv));
					}
				}
			} catch (InvalidUsingException e1) {
				e1.printStackTrace();
			}
		}
		presentation.add(zoomFactor, originCoord, props);

	}

	private void insertSlide() {
		double zoomFactor = diagramViewManager.getZoomFactor();
		Point2D originCoord = diagramViewManager.toWorldCoord(0, 0);
		Map<String, String> props = new HashMap<>();

		if(diagramViewManager.getCurrentDiagram() instanceof IMindMapDiagram) {
			try {
				for(IPresentation p : diagramViewManager.getCurrentDiagram().getPresentations()) {
					if(p instanceof INodePresentation) {
						INodePresentation np = (INodePresentation)p;
						boolean npv = isSubTopicVisible(np);
						props.put(np.getID(), Boolean.toString(npv));
					}
				}
			} catch (InvalidUsingException e1) {
				e1.printStackTrace();
			}
		}
		presentation.insert(zoomFactor, originCoord, props);
	}


	private void deleteSlide() {
		presentation.remove();
	}

	private void clearSlides() {
		presentation.clear();
	}

	private void showSlide() {
		if(presentation.getNumberOfSlides() == 0) {
			return;
		}

		if(diagramViewManager.getCurrentDiagram() instanceof IMindMapDiagram) {
			try {
				TransactionManager.beginTransaction();

				Map<String, String> props = presentation.getNodeVisibilities();
				for(IPresentation p : diagramViewManager.getCurrentDiagram().getPresentations()) {
					if(p instanceof INodePresentation) {
						INodePresentation np = (INodePresentation)p;
						if(props.containsKey(np.getID())){
							boolean npv = Boolean.parseBoolean(props.get(np.getID()));
							System.out.println("nodeVisibilities:" + np.getID() + "=" + npv);
							setSubtopicvisibility(np, npv);
						}
					}
				}
				TransactionManager.endTransaction();
			} catch (InvalidUsingException e) {
				TransactionManager.abortTransaction();
				e.printStackTrace();

			}
		}

		double zoomFactor = presentation.getZoomFactor();
		diagramViewManager.zoom(zoomFactor, true);

		Point2D originCoord = diagramViewManager.toWorldCoord(0, 0);
		Point2D targetCoord = presentation.getTargetCoord();
		new Thread(() -> {
			int STEP = 50;
			for(int i = 0; i < STEP; i++) {
				diagramViewManager.pan(
						(targetCoord.getX() - originCoord.getX()) / STEP,
						(targetCoord.getY() - originCoord.getY()) / STEP
						);
				try {
					Thread.sleep(5);
				}catch(InterruptedException ex) {
				}
			}
		}).start();

	}

	private void updatePresentationPanel() {
		numButton.setText(presentation.getCurrentSlideIndex() + "/" + presentation.getNumberOfSlides());
	}

	private static final String SUB_TOPIC_VISIBILITY = "sub_topic_visibility";
	private static final String TRUE = "true";
	private static final String FALSE = "false";

	private boolean isSubTopicVisible(INodePresentation np){
		return np.getProperty(SUB_TOPIC_VISIBILITY).equals(TRUE);
	}

	private void setSubtopicvisibility(INodePresentation np, boolean npv) {
		try {
			np.setProperty(SUB_TOPIC_VISIBILITY, npv ? TRUE : FALSE);
		}catch(InvalidEditingException e){
			e.printStackTrace();
		}
	}

	/**
	 * プロジェクトが変更されたら表示を更新する
	 */
	@Override
	public void projectChanged(ProjectEvent e) {
		updateDiagramView();
	}
	@Override
	public void projectClosed(ProjectEvent e) {
		// Do nothing when project is closed
	}

	@Override
	public void projectOpened(ProjectEvent e) {
		// Do nothing when project is opened
	}

	/**
	 * 図の選択が変更されたら表示を更新する
	 */
	@Override
	public void diagramSelectionChanged(IDiagramEditorSelectionEvent e) {
		updateDiagramView();
	}

	/**
	 * 要素の選択が変更されたら表示を更新する
	 */
	@Override
	public void entitySelectionChanged(IEntitySelectionEvent e) {
		updateDiagramView();
	}

	/**
	 * 表示を更新する
	 */
	private void updateDiagramView() {
		try {

			deactivateButtons();

			IDiagram diagram = diagramViewManager.getCurrentDiagram();
			if(diagram == null){
				return;
			}

			// 選択項目を図を読み上げる
			IPresentation[] ps = diagramViewManager.getSelectedPresentations();
			INodePresentation np = null;
			for(IPresentation p : ps){
				if(p instanceof INodePresentation){
					INodePresentation cnp = (INodePresentation)p;
					String cmessage = cnp.getLabel();
					if(Objects.nonNull(cmessage) && ! cmessage.isEmpty()){
						np = cnp;
						break;
					}
				}
			}

			if(np == null){
				logger.log(Level.WARNING, "no selected node presentation with message");
				return;
			}


			logger.log(Level.INFO, "update diagram view.");

			/*
			logger.log(Level.INFO, "np properties");
			@SuppressWarnings("unchecked")
			HashMap<String, String> props = np.getProperties();
			StringBuilder sb = new StringBuilder();
			for(Map.Entry<String, String> entry : props.entrySet()){
				sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
			}
			logger.log(Level.INFO, sb::toString);
			 */

			activateButtons();

		}catch(Exception e){
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	// IPluginExtraTabView
	@Override
	public void addSelectionListener(ISelectionListener listener) {
		// Do nothing
	}

	@Override
	public void activated() {
		// リスナーへの登録
		addDiagramListeners();
	}

	@Override
	public void deactivated() {
		// リスナーへの削除
		removeDiagramListeners();
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getTitle() {
		return title;
	}

}
