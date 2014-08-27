$(function() {

		});
function exportData() {
	location = "eventLogManager/eventLog!exportData.action?cloudContext.params.qName="
			+ $('#qName').val()
			+ "&cloudContext.params.qDomain="
			+ $('#qDomain').val()
			+ "&cloudContext.params.startDate="
			+ $('#d4331').val()
			+ "&cloudContext.params.endDate="
			+ $('#d4332').val();
}