/**
 * Overlay class to produce an overlay. 
 * 
 * TODO add an element that is over the overlay, so the content is not affected by the opacity. Add all new elements not to the overlay, add them to the inline element.
 * @see "http://css-tricks.com/css-transparency-settings-for-all-broswers/"
 * @see "http://css-tricks.com/non-transparent-elements-inside-transparent-elements/"
 * @param overlayId
 * @param dataName
 * @returns
 */
function overlay(overlayId, dataName){
	this.id = overlayId;
	this.dataName = dataName;
	this.elemClass = 'hasOverlay';
	this.css = {
	   'width' 					: $(window).width(),
	   'height' 				: $(window).width(),
	   'position' 				: 'fixed',
	   'top' 					: 0,
	   'left' 					: 0,
	   'opacity'				: 0.5,
	   'background-color'		: 'grey',
	   'z-index'				: 100
	};
	this.overlay = $();
	this.isAppend = false;
	
	this.init = init;
	function init(){
    	this.overlay = $('<div>',
    	   {
    	       'id' : this.id,
    	       'style' : 'display:none;',
    	       'css' : this.css
    	   }
    	);
    	
		$(window).resize(function() {
			$('.hasOverlay').each(function(i,elem){
				
				var overlay = $(elem).data(dataName);
				if(overlay && overlay.isAppend){
					overlay.setWidth($(window).width());
					overlay.setHeight($(window).height());
					console.log('overlay added');
				}
			});
		});
	}
	
	this.init();

	
	this.appendTo = appendTo;
	function appendTo(elem){
	   if(!$(elem).is('.' + this.elemClass)){
       	   this.overlay.appendTo(elem);
       	   $(elem).addClass(this.elemClass);
       	   elem.data(this.dataName, this);
       	   this.isAppend = true;
	   } else {
	       console.log('this elem has already an overlay');
	   }
	   return this;
	}
	
	this.setWidth = setWidth;
	function setWidth(width){
	   this.css.width = width;
	   this.overlay.css('width', width);
       return this;
	}
	
	this.setHeight = setHeight;
	function setHeight(height){
        this.css.height = height;
    	this.overlay.css('height', height);
        return this;
	}
	
	this.show = show;
	function show(){
	   this.overlay.css('display', 'block');
	   console.log('show');
       return this;
	}
	
	this.hide = hide;
	function hide(){
		this.overlay.css('display', 'none');
	   console.log('hide');
       return this;
	}
	
	this.setOpacity = setOpacity;
	function setOpacity(value){
		this.css.opacity = value;
		this.overlay.css('opacity', value);
	}
	
	this.setBackgroundColor = setBackgroundColor;
	function setBackgroundColor(color){
		this.css['background-color'] = color;
		this.overlay.css('background-color', color);		
	}
	
	this.width = width;
	function width(){
		return this.overlay.width();
	}
	
	this.innerWidth = innerWidth;
	function innerWidth(){
		var outerPadding = parseInt(this.overlay.css('padding-left')) + parseInt(this.overlay.css('padding-right'));
		return this.overlay.width()-outerPadding;
	}
	
	this.getOverlay = getOverlay;
	function getOverlay(){
		return this.overlay;
	}
	
	this.isShown = isShown;
	function isShown(){
		return this.overlay.css('display') == 'none' ? false : true;
	}
	
	this.addLayer = addLayer;
	function addLayer(obj){
		if(this.isAppend && obj && typeof(obj) != 'undefined'){
			this.getOverlay().parent().append(obj);
		}
	}
}