var app = angular.module('smarti18n-com', ['pascalprecht.translate']);

app.config(['$translateProvider', function ($translateProvider) {
    $translateProvider
        .useUrlLoader('https://messages.smarti18n.com/api/1/messages/findForAngularMessageSource?projectId=smarti18n-com&locale=de')
        .preferredLanguage('de')
        .useSanitizeValueStrategy('sanitizeParameters');
}]);

app.controller("");

(function ($) {
    $(function () {

        $('.button-collapse').sideNav();

    }); // end of document ready
})(jQuery); // end of jQuery name space