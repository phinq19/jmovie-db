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
			imagewallLayer.append(createElement(elem.imgDef, elem.imgBig, elem.title, this.elemWidth, this.elemHeight));
		});
		this.imagewallLayer = imagewallLayer;
		overlayObj.addLayer(this.imagewallLayer);
		
		$(window).scroll(function() {
			var elemPosTop = undefined;
			var posTop = $(document).scrollTop();
			var posBottom = $(document).scrollTop() + $(window).height();
			$('#imagewall .elementWrapper img.imgUnload').each(function(index, elem){
				elemPosTop = $(this).position().top;
				if((elemPosTop > posTop) && (elemPosTop < posBottom)){
					$(this).attr('src', $(this).data('img'));
				}
			});
		});
		$(window).scroll();
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
				'overflow'			: 'hidden'
			},
			'href'	: imgSrcBig
		});
		
		if(imgSrcDef){
			var imgElem = $('<img />', {
				'src' 		: '',
				'class'		: 'imgUnload',
				'title'		: title,
				'alt'		: title,
				'css'		: {
					'width'	: maxWidth,
					'height': maxHeight
				},
				'data'		: {
					'img'	: imgSrcDef
				}
			});
		} else {
			var imgElem = $('<div>', {
				'css'		: {
					'background-color'	: 'white'
				}
			});
		}
		
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
				'background-color'	: 'white',
				'text-align'		: 'center',
				'font-size'			: '0.8em'
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

