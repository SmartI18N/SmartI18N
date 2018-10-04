var app = angular.module('smarti18n-com', ['pascalprecht.translate']);

app.config(['$translateProvider', function ($translateProvider) {
    $translateProvider
        .useUrlLoader('https://messages.smarti18n.com/api/1/messages/findForAngularMessageSource?projectId=smarti18n-com&locale=de')
        .preferredLanguage('DE')
        .useSanitizeValueStrategy('sanitizeParameters');

    $translateProvider.preferredLanguage('DE');
}]);

app.controller('RegisterController', function ($scope, $http) {
    $scope.mail = '';
    $scope.password = '';

    $scope.register = function () {
        $http.get('https://messages.smarti18n.com/api/1/users/register?mail=' + $scope.mail + '&password=' + $scope.password)
            .then(function successCallback(response) {

            }, function errorCallback(response) {

            });
    }
});

(function ($) {
    $(function () {

        $('.button-collapse').sideNav();

    });
})(jQuery);
