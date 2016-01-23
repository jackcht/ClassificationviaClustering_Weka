/*
 * File: ClassificationViaClusteringModified.java
 * Auth: Chen Haoting (chen0783@e.ntu.edu.sg)
 * Date: 2014-11-07
 * Desc: This program is used in the project of CZ4032 Data Minining in Nanyang Technological Univeristy (SG).
 * 		This program is modified from ClassificationViaClustering.java which is an additional package of Weka. 
 * 		The original code and package can be download from weka code source in sourceforge.net. 
 * 		This program is using KMeans Clustering algorithm for classification. 
 *      The algorithm can cluster the dataset into user-defined numbers of clusters and cluster the input instance into most close cluster.
 *      Classification is based on the dominant class of the cluster where the input instance clustered into. 
 * 
 * ------------------------------------------------------------------------------------
 */



package weka.classifiers.meta;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.rules.ZeroR;
import weka.clusterers.AbstractClusterer;
import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeansModified;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.RevisionUtils;
import weka.core.Utils;
//import weka.core.logging.Logger;

public class ClassificationViaClusteringModified extends AbstractClassifier {
	/** for serialization 
	 *  [chen0783]copied from ClassificationViaClustering
	 */
	private static final long serialVersionUID = -5687069451420259135L;

	  /** the cluster algorithm used (template) */
	  //protected Clusterer m_Clusterer = new SimpleKMeans();
	  protected Clusterer m_Clusterer = new SimpleKMeansModified();
	
	  /** [chen0783] We are using KMeans so no need this attribute */
	  ///** the actual cluster algorithm being used */
	  //protected Clusterer m_ActualClusterer;

	  /** the original training data header */
	  protected Instances m_OriginalHeader;
	
	  /** the modified training data header */
	  protected Instances m_ClusteringHeader;
	
	  /** the mapping between clusters and classes */
	  protected double[] m_ClustersToClasses;
	
	  /** the default model */
	  protected Classifier m_ZeroR;
	
	  /**
	   * default constructor
	   */
	  public ClassificationViaClusteringModified() {
	    super();
	  }
	
	  /**
	   * Returns a string describing classifier
	   * [chen0783]  modified global info 
	   *
	   * @return a description suitable for displaying in the explorer/experimenter
	   *         gui
	   */
	  public String globalInfo() {
	    return "Modified ClassificationViaClustering class for classification using KMeans";
	  }
	
	  /**
	   * Gets an enumeration describing the available options.
	   * 
	   * @return an enumeration of all the available options.
	   */
	  @Override
	  public Enumeration<Option> listOptions() {
	
	    Vector<Option> result = new Vector<Option>();
	
	    result.addElement(new Option("\tFull name of clusterer.\n" + "\t(default: "
	      + defaultClustererString() + ")", "W", 1, "-W"));
	
	    result.addAll(Collections.list(super.listOptions()));
	
	    result.addElement(new Option("", "", 0, "\nOptions specific to clusterer "
	      + m_Clusterer.getClass().getName() + ":"));
	
	    result
	      .addAll(Collections.list(((OptionHandler) m_Clusterer).listOptions()));
	
	    return result.elements();
	  }
	
	  /**
	   * returns the options of the current setup
	   * 
	   * @return the current options
	   */
	  @Override
	  public String[] getOptions() {
	
	    Vector<String> result = new Vector<String>();
	
	    result.add("-W");
	    result.add("" + getClusterer().getClass().getName());
	
	    Collections.addAll(result, super.getOptions());
	
	    if (getClusterer() instanceof OptionHandler) {
	      String[] options = ((OptionHandler) getClusterer()).getOptions();
	      if (options.length > 0) {
	        result.add("--");
	        Collections.addAll(result, options);
	      }
	    }
	
	    return result.toArray(new String[result.size()]);
	  }
	
	  /**
	   * Parses the options for this object.
	   * <p/>
	   * 
	   * <!-- options-start --> Valid options are:
	   * <p/>
	   * 
	   * <pre>
	   * -D
	   *  If set, classifier is run in debug mode and
	   *  may output additional info to the console
	   * </pre>
	   * 
	   * <pre>
	   * -W
	   *  Full name of clusterer.
	   *  (default: weka.clusterers.SimpleKMeans)
	   * </pre>
	   * 
	   * <pre>
	   * Options specific to clusterer weka.clusterers.SimpleKMeans:
	   * </pre>
	   * 
	   * <pre>
	   * -N &lt;num&gt;
	   *  number of clusters.
	   *  (default 2).
	   * </pre>
	   * 
	   * <pre>
	   * -V
	   *  Display std. deviations for centroids.
	   * </pre>
	   * 
	   * <pre>
	   * -M
	   *  Replace missing values with mean/mode.
	   * </pre>
	   * 
	   * <pre>
	   * -S &lt;num&gt;
	   *  Random number seed.
	   *  (default 10)
	   * </pre>
	   * 
	   * <!-- options-end -->
	   * 
	   * @param options the options to use
	   * @throws Exception if setting of options fails
	   */
	  @Override
	  public void setOptions(String[] options) throws Exception {	
		  String tmpStr;

		    tmpStr = Utils.getOption('W', options);
		    if (tmpStr.length() > 0) {
		      setClusterer(AbstractClusterer.forName(tmpStr, null));
		      setClusterer(AbstractClusterer.forName(tmpStr,
		        Utils.partitionOptions(options)));
		    } else {
		      setClusterer(AbstractClusterer.forName(defaultClustererString(), null));
		      setClusterer(AbstractClusterer.forName(defaultClustererString(),
		        Utils.partitionOptions(options)));
		    }

		    super.setOptions(options);

		    Utils.checkForRemainingOptions(options);
	  }
	
	  /**
	   * String describing default clusterer.
	   * 
	   * @return the classname
	   */
	  protected String defaultClustererString() {
	    return SimpleKMeansModified.class.getName();
	  }
	
	  /**
	   * Returns the tip text for this property
	   * 
	   * @return tip text for this property suitable for displaying in the
	   *         explorer/experimenter gui
	   */
	  public String clustererTipText() {
	    return "The clusterer to be used.";
	  }
	
	  /**
	   * Set the base clusterer
	   * 
	   * @param value the clusterer to use.
	   * @throws Exception 
	   */
	  public void setClusterer(Clusterer value) throws Exception {
		  if ( value instanceof SimpleKMeansModified )
			{
			  m_Clusterer = value;
			}
			else{
				throw new Exception("Current implementation only support for KMeans");
			}
	  }
	  
	
	  /**
	   * Get the clusterer used as the base learner.
	   * 
	   * @return the current clusterer
	   */
	  public Clusterer getClusterer() {
	    return m_Clusterer;
	  }
	
	  /**
	   * Classifies the given test instance.
	   * 
	   * @param instance the instance to be classified
	   * @return the predicted most likely class for the instance or
	   *         Utils.missingValue() if no prediction is made
	   * @throws Exception if an error occurred during the prediction
	   */
	  @Override
	  public double classifyInstance(Instance instance) throws Exception {
		//Logger.log(Logger.Level.INFO, " INSIDE classifyInstance ");
	    double result;
	    double[] values;
	    Instance newInst;
	    int i;
	    int n;
	
	    if (m_ZeroR != null) {
	      result = m_ZeroR.classifyInstance(instance);
	    } else {
	      if (m_Clusterer != null) {
	        // build new instance
	        values = new double[m_ClusteringHeader.numAttributes()];
	        n = 0;
	        for (i = 0; i < instance.numAttributes(); i++) {
	          if (i == instance.classIndex()) {
	            continue;
	          }
	          values[n] = instance.value(i);
	          n++;
	        }
	        newInst = new DenseInstance(instance.weight(), values);
	        newInst.setDataset(m_ClusteringHeader);
	
	        // determine cluster/class
	        result = m_ClustersToClasses[m_Clusterer.clusterInstance(newInst)];
	        if (result == -1) {
	          result = Utils.missingValue();
	        }
	      } else {
	        result = Utils.missingValue();
	      }
	    }
	
	    return result;
	  }
	  
	
	  /**
	   * Returns default capabilities of the classifier.
	   * 
	   * @return the capabilities of this classifier
	   */
	  @Override
	  public Capabilities getCapabilities() {
	    Capabilities result;
	
	    result = m_Clusterer.getCapabilities();
	
	    // class
	    result.disableAllClasses();
	    result.disable(Capability.NO_CLASS);
	    result.enable(Capability.NOMINAL_CLASS);
	    
	    return result;
	  }
	
	  /**
	   * builds the classifier
	   * 
	   * @param data the training instances
	   * @throws Exception if something goes wrong
	   */
	  @Override
	  public void buildClassifier(Instances data) throws Exception {
		//Logger.log(Logger.Level.INFO, " INSIDE buildClassifier ");
	    Instances clusterData;
	    int i;
	    double[] best;
	    
	    // can classifier handle the data?
	    getCapabilities().testWithFail(data);
	
	    // remove instances with missing class
	    data = new Instances(data);
	    data.deleteWithMissingClass();
	
	    // save original header (needed for clusters to classes output)
	    m_OriginalHeader = new Instances(data, 0);
	
		// remove class attribute for clusterer
	    clusterData = new Instances(data);
	    Instances clusterData2 = new Instances(data);
	    clusterData.setClassIndex(-1);
	    clusterData.deleteAttributeAt(m_OriginalHeader.classIndex());
		
	    m_ClusteringHeader = new Instances(clusterData, 0);
	
	    if (m_ClusteringHeader.numAttributes() == 0) {
	      System.err
	        .println("Data contains only class attribute, defaulting to ZeroR model.");
	      m_ZeroR = new ZeroR();
	      m_ZeroR.buildClassifier(data);
	    } else {
	      m_ZeroR = null;
	
	      // build clusterer
	      m_Clusterer.buildClusterer(clusterData2);
	      /** [chen0783] Modified clusters to classes mapping function*/
	      int numOfCluster = ((SimpleKMeansModified) m_Clusterer).getNumClusters();
	      best = new double[numOfCluster+1];
	      
	      int nominalCounts[][][] = ((SimpleKMeansModified) m_Clusterer).getClusterNominalCounts();
	      int classIndexNum = nominalCounts[0].length;
	      
	      /** [chen0783] define the class for the cluster based on the dominant class*/
		  for (i=0; i<numOfCluster; i++){	  
	    	  if (nominalCounts[i][classIndexNum-1][0] > nominalCounts[i][classIndexNum-1][1])
	    		  best[i] = 0;
	    	  else 
	    		  best[i] = 1;
	      }
   
	      m_ClustersToClasses = new double[best.length];
	      
	      System.arraycopy(best, 0, m_ClustersToClasses, 0, best.length);

	    }
	  }
	  
	
	  /**
	   * Returns a string representation of the classifier.
	   * 
	   * @return a string representation of the classifier.
	   */
	  @Override
	  public String toString() {
	    StringBuffer result;
	    int i;
	    int n;
	    boolean found;
	
	    result = new StringBuffer();
	
	    // title
	    result.append(this.getClass().getName().replaceAll(".*\\.", "") + "\n");
	    result.append(this.getClass().getName().replaceAll(".*\\.", "")
	      .replaceAll(".", "=")
	      + "\n");
	
	    // model
	    if (m_Clusterer != null) {
	      // output clusterer
	      result.append(m_Clusterer + "\n");
	
	      // clusters to classes
	      result.append("Clusters to classes mapping:\n");
	      for (i = 0; i < m_ClustersToClasses.length - 1; i++) {
	        result.append("  " + (i + 1) + ". Cluster: ");
	        if (m_ClustersToClasses[i] < 0) {
	          result.append("no class");
	        } else {
	          result.append(m_OriginalHeader.classAttribute().value(
	            (int) m_ClustersToClasses[i])
	            + " (" + ((int) m_ClustersToClasses[i] + 1) + ")");
	        }
	        result.append("\n");
	      }
	      result.append("\n");
	
	      // classes to clusters
	      result.append("Classes to clusters mapping:\n");
	      for (i = 0; i < m_OriginalHeader.numClasses(); i++) {
	        result.append("  " + (i + 1) + ". Class ("
	          + m_OriginalHeader.classAttribute().value(i) + "): ");
	
	        found = false;
	        for (n = 0; n < m_ClustersToClasses.length - 1; n++) {
	          if (((int) m_ClustersToClasses[n]) == i) {
	            found = true;
	            result.append((n + 1) + ". Cluster");
	            break;
	          }
	        }
	
	        if (!found) {
	          result.append("no cluster");
	        }
	
	        result.append("\n");
	      }
	
	      result.append("\n");
	    } else {
	      result.append("no model built yet\n");
	    }
	
	    return result.toString();
	  }
	
	  /**
	   * Returns the revision string.
	   * 
	   * @return the revision
	   */
	  @Override
	  public String getRevision() {
	    return RevisionUtils.extract("$Revision: 0001 $");
	  }
	
	  /**
	   * Runs the classifier with the given options
	   * 
	   * @param args the commandline options
	   */
	  public static void main(String[] args) {
	    runClassifier(new ClassificationViaClusteringModified(), args);
	  }
}
	
	
