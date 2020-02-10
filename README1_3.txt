10 February 2020
Pain(t) Submission 3
Matthew Bickelhaupt 
CS250

The program is compiled of one main function with new classes for different nodes:
	myBox
	myButton
	myCanvas
	myBorderPane

In addition, there are new functions to make the main function cleaner and easier to read and sort:
	saveCheck();
	open();
	undo();
	resize();

Components Include:
-Open Image from file
-Save Image
-Close program
-Menu Bar: File, Save, Go back, Properties, Edit
-Save, Save As, Open, New
-saveCheck() will provide a double check window to ensure you want to save the changes made
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
-New button will open a blank canvas, and double check using saveCheck()
-Control+S will bring up saveCheck() and save
-Control+O will open a new image file
-Now supports save/open from .jpg, .png, and .gif files

https://github.com/mattbickelhaupt/CS250
