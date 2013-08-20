function imagewall(overlayObj, imageList, elemWidth, elemHeight){
	this.totalWidth = $(overlayObj).innerWidth;
	this.elemWidth = elemWidth;
	this.elemHeight = elemHeight;
	
	if(typeof(overlayObj) != 'undefined' && typeof(imageList) == 'object'){
		$(imageList).each(function(index, elem){
			overlayObj.getOverlay().append(createElement('a' + index, 'b' + index, 'c' + index));
		});
	}
	
	this.createElement = createElement;
	function createElement(imgSrcDef, imgSrcBig, title){
		var maxElements = this.totalWidth / 154;
		var unusedSpace = this.totalWidth % 154;
		var unusedSpaceForElement = 32 / 8.2;
		
		
		
		return picElem = $('<div>', {
			'class' : 'elementWrapper',
			'css' : {
				'width':elemWidth,
				'height':elemHeight,
				'background-color' : 'black',
				'margin-left':unusedSpaceForElement / 2,
				'margin-right' : unusedSpaceForElement / 2,
				'margin-bottom' : 10,
				'float' : 'left'
			},
			
			'html' : '<a style="float:left;background-color:white; width:90%; height:90%;" href="' + imgSrcBig + '"><img src="' + imgSrcDef + '" title="' + title + '" /></a>'
			
		});
	}
	
}

