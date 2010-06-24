Ext.onReady(function(){

    Ext.app.REMOTING_API.enableBuffer = 0;
    Ext.Direct.addProvider(Ext.app.REMOTING_API);

    // provide feedback for any errors
    Ext.QuickTips.init();

   var languagesStore = new Ext.data.SimpleStore( {
    fields: ['name'],
    data: [['Java'], ['Javascript'], ['C++'], ['Perl'], ['Python'], ['Groovy'], ['Scala']]
  }) 
  
  var form = new Ext.FormPanel({    
    renderTo: Ext.getBody(),
    frame: true,
    fileUpload : true,
    width: 500,
    labelWidth: 180,
    defaults: {
      width: 300
    },
    defaultType: 'textfield',      
    items: [
      {
        xtype: 'datefield',
        fieldLabel: 'Pick a date',
        name: 'datefield'
      },
      {
        xtype: 'combo',
        name: 'combo',
        fieldLabel: 'Favourite language',
        mode: 'local',
        store: languagesStore,
        displayField: 'name',
        forceSelection: true,
        triggerAction: 'all',
        valueField: 'name'
      },
      {
        xtype: 'fileuploadfield',
        buttonOnly: false,
        id: 'form-file',
        fieldLabel: 'File (MUST be a text file)',
        name: 'fileUpload',
        buttonCfg: {
          text: '...'
        }
      },
      textArea1 = new Ext.form.TextArea( {
        name: 'textArea', 
        fieldLabel: "File contents",
        height: 100,
        disabled: true
      }),
      {
        xtype: 'checkbox',
        fieldLabel: 'Check if you like springextjs!',
        name : 'checkbox',
        width: 15 
      }
    ],
    api: {        
      submit: uploadController.uploadTest
    },    
    buttons: [ 
      {
        text: "Submit",
        handler: function() {
          form.getForm().submit({
            success: function(form, action) {              
              textArea1.setValue( action.result.fileContents);
            }
          });
          
          
//          FormPostDemo.handleSubmit(form.getForm().el, function(result, e){
//            if( e.type === 'exception') {
//              Ext.MessageBox.alert("Unexpected server error", e.message );
//              return;
//            }
//            Ext.MessageBox.alert("Posted values", result.fieldNamesAndValues);
//            textArea1.setValue( result.fileContents);
//          });
        }
      },
      {
        text: "Reset",
        handler: function(){
          form.getForm().reset();
        }
      }
    ]
  });
  
  
  

});