$(function(){
        $('#teste').change(function(){
                var obj = $(this);		                
		var value = obj.val();               
                 $('#teste').val(value);
        });        
        
	$('select.select-ajax').change(function(){
		var obj = $(this);
		var page = obj.data('link'),
                
		value = obj.val(),                
		param = obj.data('param'),
		result = obj.data('result');                
		var result_obj = $('#'+result);
		var selected = result_obj.data('selected');
		if(selected)
			result_obj.data('selected','');
		result_obj.find('option:gt(0)').remove();
		if(page && result && value){
			var first_option = result_obj.find('option:eq(0)');
			first_option.data('default-html',first_option.html());
			first_option.html('Carregando...');
			$.getJSON(page+'?'+param+'='+value, function(data) {
				var items;
				$.each( data, function( key, val ) {
					items += "<option value='" + key + "'";
					if(key==selected)
						items += " selected";
					items += ">" + val + "</option>";
				});
				first_option.html(first_option.data('default-html'));
				result_obj.append(items);
			});
		}
	}).trigger('change');
})