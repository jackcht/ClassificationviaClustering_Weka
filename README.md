# ClassificationviaClustering_Weka

Modified Classification via Clustering package of Weka

#----------------------------------------------#
Run WEKA (Project) in Eclipse

Initial Setup in Eclipse
------------------------------------------------

1. First, you may need to download the Eclipse Standard for your platform and install it.

2. You should choose the 'File>Import' function and choose 'General>Existing Projects into Workspace' to import our 'WekaClustering' project. 

3. Find 'Java Build Path' in the 'Properties' of the project and 'Add External JARs' in the 'Libraries' tab. The required 'weka.jar' (NOT weka-src.jar) is in the weka install path (in Windows the default path is C:\Program Files\Weka-3-7).


Run the project as Weka Application
----------------------------------------------
1. Right-click the project in the Package Explorer on the left side, Run As -> Java Application. 

2. In the pop-up window, you need to select type for the Java Application. 
    Select 'GUIChooser - weka.gui' in the list and click OK button.
    Then the "WEKA GUI Chooser" will be launched.
    You will then see the familiar WEKA interface.



Classification Processes
-----------------------------------------------
1. Select Weka Explorer

2. In the Preprocess Tab, choose 'Open file' to load our dataset (arff file)

3. In the Classify Tab, choose the 'ClassificationViaClusteringModified' under weka/classifiers/meta. 

4. You can click on the 'SimpleKMeansModified' on the clusters row to specify option of the K-Means algorithm. For example, you can change the 'numClusters' to 10~30 for a good accurate result. (The clustering method is fixed to be SimpleKMeansModified and cannot be changed)

5. Choose the 'Test Options' on the left of the Weka Explorer panel 
(NOTE: Cross-validation (e.g. Folds = 10) can be very slow for validating our datasets without subset selection (i.e. full attributes))

6. Click on 'Start', the result will show on the 'Classifier Output' on the right of the Weka Explorer Panel. 
