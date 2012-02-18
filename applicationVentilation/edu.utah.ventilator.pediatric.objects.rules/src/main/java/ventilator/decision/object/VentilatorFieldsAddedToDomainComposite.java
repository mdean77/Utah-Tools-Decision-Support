package ventilator.decision.object;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.swt.widgets.Composite;

import core.decision.object.DomainFieldsCompositeAdded;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DateTime;
import com.swtdesigner.SWTResourceManager;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;

@SuppressWarnings("unused")
public class VentilatorFieldsAddedToDomainComposite extends
		DomainFieldsCompositeAdded {


	private DateTime observationTime;
	private Label observationTimeLabel;
	private DateTime observationDate;
	private Label observationDateLabel;
	private Button refreshDataButton;
	private Text text;
	private Text pHText;
	private Text PaO2Text;
	private Text pCO2Text;
	private Text FiO2Text;
	private Text MAPText;
	private Text SpO2Text;
	private Text ventilatorRateText;
	private Text tidalVolumePerKgText;
	private Text PEEPText;
	private Text PIPText;
	private Text pHText_2;
	private Text PaO2Text_2;
	private Text pCO2Text_2;
	private Text FiO2Text_2;
	private Text MAPText_2;
	private Text SpO2Text_2;
	private Text ventilatorRateText_2;
	private Text tidalVolumePerKgText_2;
	private Text PEEPText_2;
	private Text PIPText_2;
	private Text pHText_3;
	private Text PaO2Text_3;
	private Text pCO2Text_3;
	private Text FiO2Text_3;
	private Text SpO2Text_3;
	private Text MAPText_3;
	private Text FreqText_3;
	private Text AmpText_3;
	private Text pHText_4;
	private Text PaO2Text_4;
	private Text pCO2Text_4;
	private Text FiO2Text_4;
	private Text SpO2Text_4;
	private Text MAPText_4;
	private Text ventilatorRateText_4;
	private Text tidalVolumePerKgText_4;
	private Text PEEPText_4;
	private Text PlateauPressureText_4;
	private CTabFolder ventilatorTabFolder;
	private Button btnPressureControl;
	private CTabItem tbtmVolumeControl;
	private CTabItem tbtmHighFrequency;
	private CTabItem tbtmPressureRegulatedVc;
	private CTabItem tbtmPressureControl;
	protected CTabItem[] tabs;
	private Button btnPrvc;
	private Button btnHfov;
	private Button btnVolumeControl;
	private Button btnNewAbg;
	private Label lblLastDate_pH_2;
	private Label lblLastDate_PaO2_2;
	private Label lblLastDate_PCO2_2;
	private Label lblLastDate_pH;
	private Label lblLastDate_PaO2;
	private Label lblLastDate_PCO2;
	private Label lblLastDate_pH_3;
	private Label lblLastDate_PaO2_3;
	private Label lblLastDate_PCO2_3;
	private Label lblLastDate_pH_4;
	private Label lblLastDate_PaO2_4;
	private Label lblLastDate_PCO2_4;

	

	public VentilatorFieldsAddedToDomainComposite(Composite parent, int style) {
		super(parent, style);
		domainFieldsComposite.setLayout(new FormLayout());
		
		Composite composite = new Composite(domainFieldsComposite, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(100, -10);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new GridLayout(6, true));

		
		createDateTimeRefreshButton(composite);
		
		Label lblObservationDate = new Label(composite, SWT.HORIZONTAL | SWT.RIGHT);
		lblObservationDate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblObservationDate.setAlignment(SWT.CENTER);
		lblObservationDate.setText("Observation Date:");
		
		observationDate = new DateTime(composite, SWT.BORDER);
		
		Label lblTimehhmm = new Label(composite, SWT.RIGHT);
		lblTimehhmm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblTimehhmm.setText("Time (hh:mm):");
		
		observationTime = new DateTime(composite, SWT.BORDER | SWT.TIME);
		
		btnNewAbg = new Button(composite, SWT.NONE);

		GridData gd_btnNewAbg = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_btnNewAbg.widthHint = 77;
		btnNewAbg.setLayoutData(gd_btnNewAbg);
		btnNewAbg.setText("New ABG");
		
		Group ventilatorModeGroup = new Group(domainFieldsComposite, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.left = new FormAttachment(0, 10);
		fd_group.top = new FormAttachment(composite, 6);
		ventilatorModeGroup.setLayoutData(fd_group);
		
	
		btnPressureControl = new Button(ventilatorModeGroup, SWT.RADIO);
		btnPressureControl.setBounds(10, 10, 113, 18);
		btnPressureControl.setText("Pressure Control");
		btnPressureControl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ventilatorTabFolder.setSelection(tbtmPressureControl);		
			}
		});

		
		btnPrvc = new Button(ventilatorModeGroup, SWT.RADIO);
		btnPrvc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ventilatorTabFolder.setSelection(tbtmPressureRegulatedVc);	
			}
		});
		btnPrvc.setBounds(10, 34, 91, 18);
		btnPrvc.setText("PRVC");
		
		btnHfov = new Button(ventilatorModeGroup, SWT.RADIO);
		btnHfov.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ventilatorTabFolder.setSelection(tbtmHighFrequency);	
			}
		});
		btnHfov.setBounds(10, 58, 91, 18);
		btnHfov.setText("HFOV");
		
		btnVolumeControl = new Button(ventilatorModeGroup, SWT.RADIO);
		btnVolumeControl.setBounds(10, 82, 113, 18);
		btnVolumeControl.setText("Volume Control");
		btnVolumeControl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ventilatorTabFolder.setSelection(tbtmVolumeControl);	
			}
		});
		
		ventilatorTabFolder = new CTabFolder(domainFieldsComposite, SWT.NONE);
		ventilatorTabFolder.setToolTipText("Editors for respective modes of ventilation");
		ventilatorTabFolder.setBorderVisible(true);
		fd_group.bottom = new FormAttachment(ventilatorTabFolder, 0, SWT.BOTTOM);
		fd_group.right = new FormAttachment(ventilatorTabFolder, -6);		
		
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.left = new FormAttachment(0, 163);
		fd_tabFolder.right = new FormAttachment(100, -10);
		fd_tabFolder.top = new FormAttachment(composite, 6);
		fd_tabFolder.bottom = new FormAttachment(100,0);
		ventilatorTabFolder.setLayoutData(fd_tabFolder);
		
		ventilatorTabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if(ventilatorTabFolder.getSelection().equals(tbtmPressureControl)){
					clearModeGroupButtons();
					btnPressureControl.setSelection(true);				
				}
				if(ventilatorTabFolder.getSelection().equals(tbtmPressureRegulatedVc)){
					clearModeGroupButtons();
					btnPrvc.setSelection(true);				
				}
				if(ventilatorTabFolder.getSelection().equals(tbtmHighFrequency)){
					clearModeGroupButtons();
					btnHfov.setSelection(true);				
				}
				if(ventilatorTabFolder.getSelection().equals(tbtmVolumeControl)){
					clearModeGroupButtons();
					btnVolumeControl.setSelection(true);				
				}
			}
		});
		
		tbtmPressureControl = new CTabItem(ventilatorTabFolder, SWT.NONE);
		tbtmPressureControl.setText("Pressure Control");
		
		Composite composite_PC = new Composite(ventilatorTabFolder, SWT.NONE);
		tbtmPressureControl.setControl(composite_PC);
		composite_PC.setLayout(new GridLayout(6, true));
		
		Label lblPh = new Label(composite_PC, SWT.NONE);
		lblPh.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPh.setText("pH:");
		
		pHText = new Text(composite_PC, SWT.BORDER);
		pHText.setToolTipText("pH from the most recent Arterial Blood Gas");
		pHText.setBackground(SWTResourceManager.getColor(255, 255, 153));
		pHText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPaO2 = new Label(composite_PC, SWT.NONE);
		lblPaO2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPaO2.setText("PaO2:");
		
		PaO2Text = new Text(composite_PC, SWT.BORDER);
		PaO2Text.setToolTipText("PaO2 from the most recent Arterial Blood Gas");
		PaO2Text.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PaO2Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblpCO2 = new Label(composite_PC, SWT.NONE);
		lblpCO2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblpCO2.setText("pCO2:");
		
		pCO2Text = new Text(composite_PC, SWT.BORDER);
		pCO2Text.setToolTipText("pCO2 from the most recent Arterial Blood Gas");
		pCO2Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_PC, SWT.NONE);
		
		lblLastDate_pH = new Label(composite_PC, SWT.NONE);
		lblLastDate_pH.setToolTipText("time of the most recent pH measurement");
		lblLastDate_pH.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_pH.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		lblLastDate_pH.setText("Last Date");
		new Label(composite_PC, SWT.NONE);
		
		lblLastDate_PaO2 = new Label(composite_PC, SWT.NONE);
		lblLastDate_PaO2.setToolTipText("time of the most recent PaO2 measurement");
		lblLastDate_PaO2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_PaO2.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		lblLastDate_PaO2.setText("Last Date");
		new Label(composite_PC, SWT.NONE);
		
		lblLastDate_PCO2 = new Label(composite_PC, SWT.NONE);
		lblLastDate_PCO2.setToolTipText("time of the most recent PCO2 measurement");
		lblLastDate_PCO2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_PCO2.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		lblLastDate_PCO2.setText("Last Date");
		
		Label lblFiO2 = new Label(composite_PC, SWT.NONE);
		lblFiO2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFiO2.setText("FiO2:");
		
		FiO2Text = new Text(composite_PC, SWT.BORDER);
		FiO2Text.setEnabled(true);
		FiO2Text.setToolTipText("FiO2 as a decimal number");
		FiO2Text.setBackground(SWTResourceManager.getColor(255, 255, 153));
		FiO2Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSpO2 = new Label(composite_PC, SWT.NONE);
		lblSpO2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpO2.setText("SpO2:");
		
		SpO2Text = new Text(composite_PC, SWT.BORDER);
		SpO2Text.setEnabled(true);
		SpO2Text.setToolTipText("O2 sat from the pulse oximeter");
		SpO2Text.setBackground(SWTResourceManager.getColor(255, 255, 153));
		SpO2Text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMap = new Label(composite_PC, SWT.NONE);
		lblMap.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMap.setText("MAP:");
		
		MAPText = new Text(composite_PC, SWT.BORDER);
		MAPText.setEnabled(true);
		MAPText.setToolTipText("Mean Airway Pressure");
		MAPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVentilatorRate = new Label(composite_PC, SWT.NONE);
		lblVentilatorRate.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVentilatorRate.setText("Ventilator Rate:");
		
		ventilatorRateText = new Text(composite_PC, SWT.BORDER);
		ventilatorRateText.setEnabled(true);
		ventilatorRateText.setBackground(SWTResourceManager.getColor(255, 255, 153));
		ventilatorRateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVtmlkg = new Label(composite_PC, SWT.NONE);
		lblVtmlkg.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVtmlkg.setText("VT (ml/kg):");
		
		tidalVolumePerKgText = new Text(composite_PC, SWT.BORDER);
		tidalVolumePerKgText.setEnabled(true);
		tidalVolumePerKgText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_PC, SWT.NONE);
		new Label(composite_PC, SWT.NONE);
		
		Label lblPeep = new Label(composite_PC, SWT.NONE);
		lblPeep.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPeep.setText("PEEP:");
		
		PEEPText = new Text(composite_PC, SWT.BORDER);
		PEEPText.setEnabled(true);
		PEEPText.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PEEPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPip = new Label(composite_PC, SWT.NONE);
		lblPip.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPip.setText("PIP:");
		
		PIPText = new Text(composite_PC, SWT.BORDER);
		PIPText.setEnabled(true);
		PIPText.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PIPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_PC, SWT.NONE);
		new Label(composite_PC, SWT.NONE);
		
		tbtmPressureRegulatedVc = new CTabItem(ventilatorTabFolder, SWT.NONE);
		tbtmPressureRegulatedVc.setText("Pressure Regulated VC");
		
		Composite composite_PRVC = new Composite(ventilatorTabFolder, SWT.NONE);
		composite_PRVC.setEnabled(false);
		tbtmPressureRegulatedVc.setControl(composite_PRVC);
		composite_PRVC.setLayout(new GridLayout(6, true));
		
		Label lblPh_2 = new Label(composite_PRVC, SWT.NONE);
		lblPh_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPh_2.setText("pH:");
		
		pHText_2 = new Text(composite_PRVC, SWT.BORDER);
		pHText_2.setToolTipText("pH from the most recent Arterial Blood Gas");
		pHText_2.setBackground(SWTResourceManager.getColor(255, 255, 153));
		pHText_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPaO2_2 = new Label(composite_PRVC, SWT.NONE);
		lblPaO2_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPaO2_2.setText("PaO2:");
		
		PaO2Text_2 = new Text(composite_PRVC, SWT.BORDER);
		PaO2Text_2.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PaO2Text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblpCO2_2 = new Label(composite_PRVC, SWT.NONE);
		lblpCO2_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblpCO2_2.setText("pCO2:");
		
		pCO2Text_2 = new Text(composite_PRVC, SWT.BORDER);
		pCO2Text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_PRVC, SWT.NONE);
		
		lblLastDate_pH_2 = new Label(composite_PRVC, SWT.NONE);
		lblLastDate_pH_2.setToolTipText("time of the most recent pH measurement");
		lblLastDate_pH_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_pH_2.setText("Last Date");
		lblLastDate_pH_2.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		new Label(composite_PRVC, SWT.NONE);
		
		lblLastDate_PaO2_2 = new Label(composite_PRVC, SWT.NONE);
		lblLastDate_PaO2_2.setToolTipText("time of the most recent PaO2 measurement");
		lblLastDate_PaO2_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_PaO2_2.setText("Last Date");
		lblLastDate_PaO2_2.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		new Label(composite_PRVC, SWT.NONE);
		
		lblLastDate_PCO2_2 = new Label(composite_PRVC, SWT.NONE);
		lblLastDate_PCO2_2.setToolTipText("time of the most recent PCO2 measurement");
		lblLastDate_PCO2_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_PCO2_2.setText("Last Date");
		lblLastDate_PCO2_2.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		
		Label lblFiO2_2 = new Label(composite_PRVC, SWT.NONE);
		lblFiO2_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFiO2_2.setText("FiO2:");
		
		FiO2Text_2 = new Text(composite_PRVC, SWT.BORDER);
		FiO2Text_2.setToolTipText("FiO2 as a decimal number");
		FiO2Text_2.setBackground(SWTResourceManager.getColor(255, 255, 153));
		FiO2Text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSpO2_2 = new Label(composite_PRVC, SWT.NONE);
		lblSpO2_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpO2_2.setText("SpO2:");
		
		SpO2Text_2 = new Text(composite_PRVC, SWT.BORDER);
		SpO2Text_2.setToolTipText("O2 sat from the pulse oximeter");
		SpO2Text_2.setBackground(SWTResourceManager.getColor(255, 255, 153));
		SpO2Text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMAP_2 = new Label(composite_PRVC, SWT.NONE);
		lblMAP_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMAP_2.setText("MAP:");
		
		MAPText_2 = new Text(composite_PRVC, SWT.BORDER);
		MAPText_2.setToolTipText("Mean Airway Pressure");
		MAPText_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVentilatorRate_2 = new Label(composite_PRVC, SWT.NONE);
		lblVentilatorRate_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVentilatorRate_2.setText("Ventilator Rate:");
		
		ventilatorRateText_2 = new Text(composite_PRVC, SWT.BORDER);
		ventilatorRateText_2.setBackground(SWTResourceManager.getColor(255, 255, 153));
		ventilatorRateText_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVtmlkg_2 = new Label(composite_PRVC, SWT.NONE);
		lblVtmlkg_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVtmlkg_2.setText("VT (ml/kg):");
		
		tidalVolumePerKgText_2 = new Text(composite_PRVC, SWT.BORDER);
		tidalVolumePerKgText_2.setBackground(SWTResourceManager.getColor(255, 255, 153));
		tidalVolumePerKgText_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_PRVC, SWT.NONE);
		new Label(composite_PRVC, SWT.NONE);
		
		Label lblPEEP_2 = new Label(composite_PRVC, SWT.NONE);
		lblPEEP_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPEEP_2.setText("PEEP:");
		
		PEEPText_2 = new Text(composite_PRVC, SWT.BORDER);
		PEEPText_2.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PEEPText_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPip_2 = new Label(composite_PRVC, SWT.NONE);
		lblPip_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPip_2.setText("PIP:");
		
		PIPText_2 = new Text(composite_PRVC, SWT.BORDER);
		PIPText_2.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PIPText_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_PRVC, SWT.NONE);
		new Label(composite_PRVC, SWT.NONE);
		
		tbtmHighFrequency = new CTabItem(ventilatorTabFolder, SWT.NONE);
		tbtmHighFrequency.setText("High Frequency Oscillatory");
		
		Composite composite_HFOV = new Composite(ventilatorTabFolder, SWT.NONE);
		composite_HFOV.setEnabled(false);
		tbtmHighFrequency.setControl(composite_HFOV);
		composite_HFOV.setLayout(new GridLayout(6, true));
		
		Label lblPh_3 = new Label(composite_HFOV, SWT.NONE);
		lblPh_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPh_3.setText("pH:");
		
		pHText_3 = new Text(composite_HFOV, SWT.BORDER);
		pHText_3.setBackground(SWTResourceManager.getColor(255, 255, 153));
		pHText_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPaO2_3 = new Label(composite_HFOV, SWT.NONE);
		lblPaO2_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPaO2_3.setText("PaO2:");
		
		PaO2Text_3 = new Text(composite_HFOV, SWT.BORDER);
		PaO2Text_3.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PaO2Text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblpCO2_3 = new Label(composite_HFOV, SWT.NONE);
		lblpCO2_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblpCO2_3.setText("pCO2:");
		
		pCO2Text_3 = new Text(composite_HFOV, SWT.BORDER);
		pCO2Text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_HFOV, SWT.NONE);
		
		lblLastDate_pH_3 = new Label(composite_HFOV, SWT.NONE);
		lblLastDate_pH_3.setToolTipText("time of the most recent pH measurement");
		lblLastDate_pH_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_pH_3.setText("Last Date");
		lblLastDate_pH_3.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		new Label(composite_HFOV, SWT.NONE);
		
		lblLastDate_PaO2_3 = new Label(composite_HFOV, SWT.NONE);
		lblLastDate_PaO2_3.setToolTipText("time of the most recent PaO2 measurement");
		lblLastDate_PaO2_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_PaO2_3.setText("Last Date");
		lblLastDate_PaO2_3.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		new Label(composite_HFOV, SWT.NONE);
		
		lblLastDate_PCO2_3 = new Label(composite_HFOV, SWT.NONE);
		lblLastDate_PCO2_3.setToolTipText("time of the most recent PCO2 measurement");
		lblLastDate_PCO2_3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_PCO2_3.setText("Last Date");
		lblLastDate_PCO2_3.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		
		Label lblFiO2_3 = new Label(composite_HFOV, SWT.NONE);
		lblFiO2_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFiO2_3.setText("FiO2:");
		
		FiO2Text_3 = new Text(composite_HFOV, SWT.BORDER);
		FiO2Text_3.setBackground(SWTResourceManager.getColor(255, 255, 153));
		FiO2Text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSpO2_3 = new Label(composite_HFOV, SWT.NONE);
		lblSpO2_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpO2_3.setText("SpO2:");
		
		SpO2Text_3 = new Text(composite_HFOV, SWT.BORDER);
		SpO2Text_3.setBackground(SWTResourceManager.getColor(255, 255, 153));
		SpO2Text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMap_3 = new Label(composite_HFOV, SWT.NONE);
		lblMap_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMap_3.setText("MAP:");
		
		MAPText_3 = new Text(composite_HFOV, SWT.BORDER);
		MAPText_3.setBackground(SWTResourceManager.getColor(255, 255, 153));
		MAPText_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFrequency_3 = new Label(composite_HFOV, SWT.NONE);
		lblFrequency_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrequency_3.setText("Frequency (Hz):");
		
		FreqText_3 = new Text(composite_HFOV, SWT.BORDER);
		FreqText_3.setBackground(SWTResourceManager.getColor(255, 255, 153));
		FreqText_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblAmplitude_3 = new Label(composite_HFOV, SWT.NONE);
		lblAmplitude_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAmplitude_3.setText("Amplitude:");
		
		AmpText_3 = new Text(composite_HFOV, SWT.BORDER);
		AmpText_3.setBackground(SWTResourceManager.getColor(255, 255, 153));
		AmpText_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_HFOV, SWT.NONE);
		new Label(composite_HFOV, SWT.NONE);
		
		tbtmVolumeControl = new CTabItem(ventilatorTabFolder, SWT.NONE);
		tbtmVolumeControl.setText("Volume Control");
		
		Composite composite_VolumeControl = new Composite(ventilatorTabFolder, SWT.NONE);
		tbtmVolumeControl.setControl(composite_VolumeControl);
		composite_VolumeControl.setLayout(new GridLayout(6, true));
		
		Label lblPh_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblPh_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPh_4.setText("pH:");
		
		pHText_4 = new Text(composite_VolumeControl, SWT.BORDER);
		pHText_4.setEditable(false);
		pHText_4.setEnabled(false);
		pHText_4.setBackground(SWTResourceManager.getColor(255, 255, 153));
		pHText_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPaO2_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblPaO2_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPaO2_4.setText("PaO2:");
		
		PaO2Text_4 = new Text(composite_VolumeControl, SWT.BORDER);
		PaO2Text_4.setEditable(false);
		PaO2Text_4.setEnabled(false);
		PaO2Text_4.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PaO2Text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblpCO2_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblpCO2_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblpCO2_4.setText("pCO2:");
		
		pCO2Text_4 = new Text(composite_VolumeControl, SWT.BORDER);
		pCO2Text_4.setEnabled(false);
		pCO2Text_4.setEditable(false);
		pCO2Text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_VolumeControl, SWT.NONE);
		
		lblLastDate_pH_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblLastDate_pH_4.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_pH_4.setText("Last Date");
		lblLastDate_pH_4.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		new Label(composite_VolumeControl, SWT.NONE);
		
		lblLastDate_PaO2_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblLastDate_PaO2_4.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_PaO2_4.setText("Last Date");
		lblLastDate_PaO2_4.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		new Label(composite_VolumeControl, SWT.NONE);
		
		lblLastDate_PCO2_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblLastDate_PCO2_4.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		lblLastDate_PCO2_4.setText("Last Date");
		lblLastDate_PCO2_4.setFont(SWTResourceManager.getFont("Lucida Grande", 8, SWT.NORMAL));
		
		Label lblFiO2_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblFiO2_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFiO2_4.setText("FiO2:");
		
		FiO2Text_4 = new Text(composite_VolumeControl, SWT.BORDER);
		FiO2Text_4.setBackground(SWTResourceManager.getColor(255, 255, 153));
		FiO2Text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSpO2_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblSpO2_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSpO2_4.setText("SpO2:");
		
		SpO2Text_4 = new Text(composite_VolumeControl, SWT.BORDER);
		SpO2Text_4.setBackground(SWTResourceManager.getColor(255, 255, 153));
		SpO2Text_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMAP_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblMAP_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMAP_4.setText("MAP:");
		
		MAPText_4 = new Text(composite_VolumeControl, SWT.BORDER);
		MAPText_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVentilatorRate_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblVentilatorRate_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVentilatorRate_4.setText("Ventilator Rate:");
		
		ventilatorRateText_4 = new Text(composite_VolumeControl, SWT.BORDER);
		ventilatorRateText_4.setBackground(SWTResourceManager.getColor(255, 255, 153));
		ventilatorRateText_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblVtmlkg_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblVtmlkg_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblVtmlkg_4.setText("VT (ml/kg):");
		
		tidalVolumePerKgText_4 = new Text(composite_VolumeControl, SWT.BORDER);
		tidalVolumePerKgText_4.setBackground(SWTResourceManager.getColor(255, 255, 153));
		tidalVolumePerKgText_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_VolumeControl, SWT.NONE);
		new Label(composite_VolumeControl, SWT.NONE);
		
		Label lblPEEP_4 = new Label(composite_VolumeControl, SWT.NONE);
		lblPEEP_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPEEP_4.setText("PEEP:");
		
		PEEPText_4 = new Text(composite_VolumeControl, SWT.BORDER);
		PEEPText_4.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PEEPText_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPlateauPressure = new Label(composite_VolumeControl, SWT.NONE);
		lblPlateauPressure.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPlateauPressure.setText("Plateau Pressure:");
		
		PlateauPressureText_4 = new Text(composite_VolumeControl, SWT.BORDER);
		PlateauPressureText_4.setBackground(SWTResourceManager.getColor(255, 255, 153));
		PlateauPressureText_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_VolumeControl, SWT.NONE);
		new Label(composite_VolumeControl, SWT.NONE);
		
	}
	
	private void clearModeGroupButtons(){
		btnHfov.setSelection(false);
		btnPressureControl.setSelection(false);
		btnVolumeControl.setSelection(false);
		btnPrvc.setSelection(false);
	}

	private void createDateTimeRefreshButton(Composite composite) {
		Button btnRefreshDateTime = new Button(composite, SWT.NONE);
		btnRefreshDateTime.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		btnRefreshDateTime.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				GregorianCalendar temp = new GregorianCalendar();
				observationDate.setDate(temp.get(Calendar.YEAR), temp.get(Calendar.MONTH), temp.get(Calendar.DAY_OF_MONTH));
				observationTime.setTime(temp.get(Calendar.HOUR_OF_DAY), temp.get(Calendar.MINUTE), 0);
				resetFocus();
			}
		});
		btnRefreshDateTime.setText("Refresh Date Time");
	}

	@Override
	public boolean allRequiredFieldsFilledIn() {
		// TODO Auto-generated method stub
		return false;
	}

	

	
	public void resetFocus() {
		//ventilatorModeGroup.setFocus ();
	}

	public Button getBtnNewAbg() {
		return btnNewAbg;
	}

	public DateTime getObservationTime() {
		return observationTime;
	}

	public DateTime getObservationDate() {
		return observationDate;
	}

	public Text getpHText() {
		return pHText;
	}

	public Text getPaO2Text() {
		return PaO2Text;
	}

	public Text getpCO2Text() {
		return pCO2Text;
	}

	public Text getpHText_2() {
		return pHText_2;
	}

	public Text getPaO2Text_2() {
		return PaO2Text_2;
	}

	public Text getpCO2Text_2() {
		return pCO2Text_2;
	}

	public Text getpHText_3() {
		return pHText_3;
	}

	public Text getPaO2Text_3() {
		return PaO2Text_3;
	}

	public Text getpCO2Text_3() {
		return pCO2Text_3;
	}

	public Text getpHText_4() {
		return pHText_4;
	}

	public Text getPaO2Text_4() {
		return PaO2Text_4;
	}

	public Text getpCO2Text_4() {
		return pCO2Text_4;
	}

	public Label getLblLastDate_pH_2() {
		return lblLastDate_pH_2;
	}

	public Label getLblLastDate_PaO2_2() {
		return lblLastDate_PaO2_2;
	}

	public Label getLblLastDate_PCO2_2() {
		return lblLastDate_PCO2_2;
	}

	public Label getLblLastDate_pH() {
		return lblLastDate_pH;
	}

	public Label getLblLastDate_PaO2() {
		return lblLastDate_PaO2;
	}

	public Label getLblLastDate_PCO2() {
		return lblLastDate_PCO2;
	}

	public Label getLblLastDate_pH_3() {
		return lblLastDate_pH_3;
	}

	public Label getLblLastDate_PaO2_3() {
		return lblLastDate_PaO2_3;
	}

	public Label getLblLastDate_PCO2_3() {
		return lblLastDate_PCO2_3;
	}

	public Label getLblLastDate_pH_4() {
		return lblLastDate_pH_4;
	}

	public Label getLblLastDate_PaO2_4() {
		return lblLastDate_PaO2_4;
	}

	public Label getLblLastDate_PCO2_4() {
		return lblLastDate_PCO2_4;
	}
}