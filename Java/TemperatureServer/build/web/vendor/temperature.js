/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var tempGauge;
var chat;

var createChat = function() {
    
    this.socket = null;
    
    var _connect = function(host) {
        if ('WebSocket' in window) {
            this.socket = new WebSocket(host);
        } else if ('MozWebSocket' in window) {
            this.socket = new MozWebSocket(host);
        } else {
            console.log('Error: WebSocket is not supported by this browser.');
            return;
        }

        this.socket.onopen = function () {
            tempGauge.setLedColor(steelseries.LedColor.GREEN_LED);
        };

        this.socket.onclose = function () {
            tempGauge.setLedColor(steelseries.LedColor.RED_LED);
        };

        this.socket.onmessage = function (message) {
            tempGauge.setValue(message.data);
        };
    };

    var _initialize = function() {
        if (window.location.protocol === 'http:') {
            _connect('ws://' + window.location.host + '/temperature/temperature');
        } else {
            _connect('wss://' + window.location.host + '/temperature/temperature');
        }
    };
    
    var _sendMessage = function() {
        socket.send('l');
    };

    return {
        initialize: _initialize,
        sendMessage: _sendMessage
    };
};

function init() {
    // by @jpmens, Sep 2013
    // from @bordignons Sep 2013
    // original idea.. http://www.desert-home.com/2013/06/how-to-use-steelseries-gauges-with.html
    // with help.. http://harmoniccode.blogspot.com.au/
    // and code.. https://github.com/HanSolo/SteelSeries-Canvas

    tempGauge = new steelseries.Radial('gaugeCanvas', {
        gaugeType: steelseries.GaugeType.TYPE4,
        minValue:-15,
        maxValue:50,
        size: 400,
        frameDesign: steelseries.FrameDesign.STEEL,
        knobStyle: steelseries.KnobStyle.STEEL,
        pointerType: steelseries.PointerType.TYPE6,
        lcdDecimals: 0,
        section: null,
        area: null,
        titleString: 'Temperatura',
        unitString: 'C',
        threshold: 100,
        lcdVisible: true,
        lcdDecimals: 2
    });
    tempGauge.setValue(''); //gives a blank display 'NaN' until broker has connected
    tempGauge.setLedColor(steelseries.LedColor.RED_LED); //set the LED RED until connected

    chat = createChat();
    chat.initialize();
}

