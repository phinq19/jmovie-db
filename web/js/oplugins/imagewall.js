function imagewall(overlayObj, imageList, elemWidth, elemHeight){
	this.totalWidth = overlayObj.innerWidth();
	this.elemWidth = elemWidth;
	this.elemHeight = elemHeight;
	this.imagewallLayer = $('<div/>', {'id' : 'imagewall', 'css' : {
		'position' 	: 'absolute',
		'top'		: 0,
		'left'		: 0,
		'z-index'	: 105
	}});
	
	if(typeof(overlayObj) != 'undefined' && typeof(imageList) == 'object'){
		var imagewallLayer = this.imagewallLayer;
		$(imageList).each(function(index, elem){
			imagewallLayer.append(createElement(elem, elem, elem, this.elemWidth, this.elemHeight));
		});
		this.imagewallLayer = imagewallLayer;
		overlayObj.addLayer(this.imagewallLayer);
	}
	
	this.createElement = createElement;
	function createElement(imgSrcDef, imgSrcBig, title, maxWidth, maxHeight){
		var maxElements = this.totalWidth / 154;
		var unusedSpace = this.totalWidth % 154;
		var unusedSpaceForElement = 32 / 8.2;
		
		var aElem = $('<a>', {
			'css'	: {
				'background-color'	: 'white',
				'width'				: '90%',
				'height'			: '90%',
				'position'			: 'absolute',
				'left'				: '5%',
				'top'				: '5%',
			},
			'href'	: imgSrcBig
		});
		
		var imgElem = $('<img />', {
			'src' 		: imgSrcDef,
			'title'		: title,
			'alt'		: title,
			'css'		: {
				'width'	: maxWidth,
				'height': maxHeight
			}
		});
		
		var titleBadge = $('<span>', {
			'class'	: 'elemTitle',
			'text'	: title,
			'css'	: {
				'position' 			: 'absolute',
				'left'				: 0,
				'bottom'			: '10%',
				'width'				: '100%',
				'border'			: '1px solid black',
				'border-left'		: 'none',
				'border-right'		: 'none',
				'background-color'	: 'lightgray',
				'text-align'		: 'center'
			}
		});
		
		var picElem = $('<div>', {
			'class' : 'elementWrapper',
			'css' : {
				'width'				: elemWidth,
				'height'			: elemHeight,
				'background-color' 	: 'black',
				'margin-left'		: unusedSpaceForElement / 2,
				'margin-right' 		: unusedSpaceForElement / 2,
				'margin-bottom' 	: 10,
				'float'				: 'left',
				'position' 			: 'relative',
				'overflow'			: 'hidden'
			}
		});
		
		return picElem.append(aElem.append(imgElem).append(titleBadge));
	}
	
}

