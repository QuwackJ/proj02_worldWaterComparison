import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides a graphical user interface to allow users to compare water data across years and countries
 * @author      Bill Barry
 * @version     2024-08-19
 */
public class WaterComparisonGui extends JFrame {

    //*************************************************************************
    //          STATIC
    //*************************************************************************
    /** color for basic services left bar */
    private static final Color BASIC1 = new Color(0, 255, 0);
    /** color for basic services right  bar */
    private static final Color BASIC2 = new Color(175, 255, 175);
    /** color for basic services at least 30 minutes a day left bar */
    private static final Color BASIC30_1 = new Color(255, 255, 0);
    /** color for basic services at least 30 minutes a day right bar */
    private static final Color BASIC30_2 = new Color(255, 255, 200);
    /** color for un-separated services left bar */
    private static final Color UNSEP1 = new Color(255, 128, 0);
    /** color for un-separated services right bar */
    private static final Color UNSEP2 = new Color(255, 175, 128);
    /** color for ground service only left bar */
    private static final Color SURFACE1 = new Color(255, 0, 0);
    /** color for ground service only right bar */
    private static final Color SURFACE2 = new Color(255, 75, 100);

    /** overall width of GUI */
    private static final int GUI_WIDTH = 800;
    /** overall height of GUI */
    private static final int GUI_HEIGHT = 325;

    /** width of graph portion of GUI */
    private static final int GRAPH_WIDTH = 520;
    /** height of graph portion of GUI */
    private static final int GRAPH_HEIGHT = 200;
    /** width of each bar to be drawn */
    private static final int BAR_WIDTH = GRAPH_WIDTH / 13;

    /** x position for combo boxes */
    private static final int COMBO_X = 20;
    /** starting y position for combo boxes */
    private static final int COMBO_STARTING_Y = 25;
    /** vertical spacing for combo boxes */
    private static final int COMBO_SPACING_Y = 40;

    //TODO: positioning of legend vs. bars could be better architected
    /** starting x position for legend */
    private static final int LEGEND_START_X = 290;
    /** starting y position for legend */
    private static final int LEGEND_START_Y = 250;
    /** horizontal spacing for legend sections */
    private static final int LEGEND_SPACING_X = 120;
    /** horizontal gap between legend items */
    private static final int LEGEND_GAP_X = 25;
    /** number of characters in the legend swatch (used to avoid additional graphical drawing) */
    private static final int LEGEND_LEADING_SPACE_COUNT = 6;

    /** colors to use for drawing legends */
    private static final Color[] LEGEND_COLORS = new Color[]{BASIC2, BASIC30_2, UNSEP2, SURFACE2};
    /** strings for legend labels  */
    private static final String[] LEGEND_LABELS = new String[]{" Basic", "Basic30", "NonSep", "Surface"};

    //*************************************************************************
    //          INSTANCE
    //*************************************************************************
    /** list of sorted water data */
    private final SortedArrayListInterface<YearlyWaterRecord> waterData;
    /** array of countries included in water data */
    private final String[] countries;
    /** array of ISO codes included in water data */
    private final String[] isoCodes;
    /** array of years covered by water data */
    private final int[] years;

    /** widget for selecting first comparison year */
    JComboBox<String> year1Combo;
    /** widget for selecting first comparison country */
    JComboBox<String> country1Combo;
    /** widget for selecting second comparison year */
    JComboBox<String> year2Combo;
    /** widget for selecting second comparison country */
    JComboBox<String> country2Combo;
    /** panel to use for drawing bars */
    JPanel drawPanel;

    //*************************************************************************
    //          CONSTRUCTOR
    //*************************************************************************
    /**
     * Creates and displays the graphical user interface
     * @param waterData     sorted list of water data for all years and countries; must not be null or empty
     * @param countries     array of countries covered; must not be null or empty; must be the same size as isoCodes array
     * @param isoCodes      array of country ISO codes; must not be null or empty; must be the same size as countries array
     * @param years         array of years covered by this data
     */
    public WaterComparisonGui(
            SortedArrayListInterface<YearlyWaterRecord> waterData,
            String[] countries,
            String[] isoCodes,
            int[] years) {

        //      Preconditions
        if (waterData == null || waterData.size() == 0) {
            throw new IllegalArgumentException("waterData must not be null, and must contain some data");
        }
        if (countries == null || countries.length == 0) {
            throw new IllegalArgumentException("countries must not be null, and must contain some data");
        }
        if (isoCodes == null || isoCodes.length == 0) {
            throw new IllegalArgumentException("isoCodes must not be null, and must contain some data");
        }
        if (countries.length != isoCodes.length) {
            throw new IllegalArgumentException("lengths of countries and isoCodes must be the same; parallel arrays required");
        }

        this.waterData = waterData;
        this.countries = countries;
        this.isoCodes = isoCodes;
        this.years = years;

        //      JFrame and main JPanel Setup
        setTitle("Water Quality Comparison");
        setSize(GUI_WIDTH, GUI_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(null);
        getContentPane().add(mainPanel);

        //      Widget Setup
        int comboCurrentY = COMBO_STARTING_Y;
        year1Combo = new JComboBox<>();
        year1Combo.setBounds(COMBO_X, comboCurrentY, 75, 30);
        comboCurrentY += COMBO_SPACING_Y;
        country1Combo = new JComboBox<>();
        country1Combo.setBounds(COMBO_X, comboCurrentY, 200, 30);
        comboCurrentY += COMBO_SPACING_Y * 2;
        year2Combo = new JComboBox<>();
        year2Combo.setBounds(COMBO_X, comboCurrentY, 75, 30);
        comboCurrentY += COMBO_SPACING_Y;
        country2Combo = new JComboBox<>();
        country2Combo.setBounds(COMBO_X, comboCurrentY, 200, 30);

        for (int year : years)  {
            year1Combo.addItem(String.valueOf(year));
            year2Combo.addItem(String.valueOf(year));
        }

        for (String country : countries) {
            country1Combo.addItem(country);
            country2Combo.addItem(country);
        }

        mainPanel.add(year1Combo);
        mainPanel.add(year2Combo);
        mainPanel.add(country1Combo);
        mainPanel.add(country2Combo);

        drawPanel = new DrawPanel();
        drawPanel.setBounds(250, 30, GRAPH_WIDTH, GRAPH_HEIGHT);
        drawPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(drawPanel);

        ComboListener comboListener = new ComboListener();
        year1Combo.addActionListener(comboListener);
        year2Combo.addActionListener(comboListener);
        country1Combo.addActionListener(comboListener);
        country2Combo.addActionListener(comboListener);

        addLegend(mainPanel, LEGEND_COLORS, LEGEND_LABELS);

        setVisible(true);
    }

    //*************************************************************************
    //          HELPER METHODS
    //*************************************************************************

    /**
     * finds the corresponding ISO code for the specified country
     * @param soughtCountry     country to look up
     * @return                  ISO code for that country; null, if not found
     */
    private String lookUpIso(String soughtCountry) {
        if (countries == null || isoCodes == null || countries.length == 0 || isoCodes.length == 0) {
            return null;
        }
        for (int index = 0; index < countries.length; index++) {
            if (soughtCountry.equals(countries[index])) {
                return isoCodes[index];
            }
        }
        return null;
    }

    /**
     * draws one bar pair in the GUI's drawing area
     * @param g                 graphics tools for drawing on the requested panel
     * @param pct1              percentage for the first bar
     * @param pct2              percentage for the second bar
     * @param color1            color for the first bar
     * @param color2            color for the second bar
     * @param currentBarX       starting x coordinate for the first bar
     */
    private static void drawSection(Graphics g, double pct1, double pct2, Color color1, Color color2, int currentBarX) {
        g.setColor(color1);
        int barHeight = (int)(Math.round(pct1 * 2));
        g.fillRect(currentBarX, GRAPH_HEIGHT - barHeight, BAR_WIDTH, barHeight);
        g.setColor(Color.BLACK);
        g.drawRect(currentBarX, GRAPH_HEIGHT - barHeight, BAR_WIDTH, barHeight);
        currentBarX += BAR_WIDTH;

        g.setColor(color2);
        barHeight = (int)(Math.round(pct2 * 2));
        g.fillRect(currentBarX, GRAPH_HEIGHT - barHeight, BAR_WIDTH, barHeight);
        g.setColor(Color.BLACK);
        g.drawRect(currentBarX, GRAPH_HEIGHT - barHeight, BAR_WIDTH, barHeight);
    }

    /**
     * draws the legend on the specified panel, with the specified colors and labels
     * @param panel     drawing panel on which to draw the legend; must not be null
     * @param colors    colors to use for the legend; must not be null, must have the same length as labels array
     * @param labels    strings to use for the legend; must not be null, must have the same length as colors array
     */
    private static void addLegend(JPanel panel, Color[] colors, String[] labels) {
        if (panel == null) {
            throw new IllegalArgumentException("panel must not be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("colors must not be null");
        }
        if (labels == null) {
            throw new IllegalArgumentException("labels must not be null");
        }
        if (colors.length != labels.length) {
            throw new IllegalArgumentException("colors and labels must contain the same number of elements");
        }

        int currentX = LEGEND_START_X;
        for (int colorAndLabelIndex = 0; colorAndLabelIndex < colors.length; colorAndLabelIndex++) {
            JLabel text = new JLabel(" ".repeat(LEGEND_LEADING_SPACE_COUNT) + labels[colorAndLabelIndex]);
            text.setOpaque(true);
            text.setBackground(colors[colorAndLabelIndex]);
            text.setBounds(currentX, LEGEND_START_Y, LEGEND_SPACING_X - LEGEND_GAP_X, 25);
            panel.add(text);
            currentX += LEGEND_SPACING_X;
        }
    }

    //*************************************************************************
    //          PRIVATE INNER CLASSES
    //*************************************************************************

    /**
     * Listener for all combo boxes
     */
    private class ComboListener implements ActionListener {
        /** creates the listener; here only to suppress -Xlint warnings */
        public ComboListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            drawPanel.repaint();
        }
    }

    /**
     * Panel to facilitate the drawing of graphics
     */
    private class DrawPanel extends JPanel {
        /** creates the panel; here only to suppress -Xlint warnings */
        public DrawPanel() {
        }

        /**
         * draws on the panel when requested by the system
         * @param g     graphics tools to draw on the corresponding panel
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Retrieve pertinent data
            String country1 = (String)country1Combo.getSelectedItem();
            String country2 = (String)country2Combo.getSelectedItem();
            String year1 = (String)year1Combo.getSelectedItem();
            String year2 = (String)year2Combo.getSelectedItem();
            String iso1 = lookUpIso(country1) + year1;
            String iso2 = lookUpIso(country2) + year2;
            YearlyWaterRecord record1 = waterData.get(waterData.indexOf(
                    new YearlyWaterRecord(iso1, 0.0, 0.0, 0.0, 0.0)));
            YearlyWaterRecord record2 = waterData.get(waterData.indexOf(
                    new YearlyWaterRecord(iso2, 0.0, 0.0, 0.0, 0.0)));

            // Draw bars in sections
            int currentBarX = 0;
            currentBarX += BAR_WIDTH;
            drawSection(g, record1.basicPlusPct(), record2.basicPlusPct(), BASIC1, BASIC2, currentBarX);
            currentBarX += BAR_WIDTH * 3;
            drawSection(g, record1.limitedPct(), record2.limitedPct(), BASIC30_1, BASIC30_2, currentBarX);
            currentBarX += BAR_WIDTH * 3;
            drawSection(g, record1.unimprovedPct(), record2.unimprovedPct(), UNSEP1, UNSEP2, currentBarX);
            currentBarX += BAR_WIDTH * 3;
            drawSection(g, record1.surfacePct(), record2.surfacePct(), SURFACE1, SURFACE2, currentBarX);
        }
    }
}
