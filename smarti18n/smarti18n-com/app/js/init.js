var app = angular.module('smarti18n-com', ['pascalprecht.translate']);

app.config(['$translateProvider', function ($translateProvider) {
   $translateProvider
       .useUrlLoader('https://messages.smarti18n.com/api/1/messages/findForAngularMessageSource?projectId=smarti18n-com&locale=de')
       .preferredLanguage('DE')
       .useSanitizeValueStrategy('sanitizeParameters');

  $translateProvider.preferredLanguage('DE');
}]);

(function ($) {
    $(function () {

        $('.button-collapse').sideNav();

    });
})(jQuery);
