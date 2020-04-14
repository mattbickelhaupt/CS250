20 April 2020
Final Pain(t) Submission
Matthew Bickelhaupt 
CS250

New Paint Updates v7.0 		(4/20/2020)
	ScrollPane was added to Properties tab to view all of README file
	Polygon coloring was fixed 
	Autosave Saves every 5 seconds rather than 10
	Window/Menu spacing and zoom is more comfortable
	"Open Squad Image" for testing was removed
	"*not Saved" Window Title was fixed for Autosave, Save As, and Ctrl+S
	
The program is compiled of one main function with new classes for different nodes:
	myBox
	myButton
	myCanvas
	myBorderPane
	myVBox
	myHBox
	myToolMenu
	Triangle

In addition, there are new functions to make the main function cleaner and easier to read and sort:
	saveCheck();
	open();
	undo();
	redo();
	resizeCanvas();
	resize();
	saveToFile();
	setLineType();
	setSPWidth();
	getChanges();
	setChanges();
	getMyWidth();
	getMyHeight();
	setSize();
	autoSave();
	getDataLoss();
	setDataLoss();

Components Include:
-Open Image from file
-Save Image
-Close program
-Menu Bar: File, Save, Go back, Properties, Edit
-Save, Save As, Open, New
-saveCheck() will provide a double check window to ensure you want to save the changes made
-Stage Title will display if project is saved or not
-resize() will open a window with width and height inputs for canvas
-colorPicker1 will set color for lines/borders
-colorPicker2 will set color for shape fill
-shapeType drop down menu will provide options for drawing:
	Free Hand
	Straight Line
	Square
	Rectangle
	Circle
	Ellipse
	Color Grabber
-lineSize drop down menu will provide numbers 1-10 for line width/border width
-resize() will increase and decrease Image view
-Undo() will remove the most recent change made by placing an old saved image on top 
-Redo() will paste the most recent undo made by placing an old saved image on top 
-New button will open a blank canvas, and double check using saveCheck()
-Control+S will bring up saveCheck() and save
-Control+O will open a new image file
-Now supports save/open from .jpg, .png, and .gif files
-Triangles, polygons can be drawn
-Text can be added to the canvas
-Eraser will draw white
-Timer will count to 5 seconds and autosave if turned on
-Stage title displays if the canvas has saved the recent changes
-After opening an image, that file type is saved
	+if saved as a smaller file type, a popup, similar to savecheck() 
	asks if possible data loss is okay
-JavaDoc comments were added to be viewed in JavaDoc form on a browser

Tool Instructions:
Choose Tool from Drop Down Menu
-For Shapes, start at top left of shape and drag to bottom right
-Grab tool, start at top left of shape and drag to bottom right
-Move tool, drag and drop selected rectangle to place
-For text tool, input your text in bottom textField, then click on canvas for location
-For polygon, input number of edges in bottom textField, then click around that number
	to add each vertex
-For triangle, click once for each vertex and after third vertext will draw your triangle 
-Button will enable/diable autosave.
https://github.com/mattbickelhaupt/CS250
