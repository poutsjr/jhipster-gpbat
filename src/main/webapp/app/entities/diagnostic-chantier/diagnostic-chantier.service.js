(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('DiagnosticChantier', DiagnosticChantier);

    DiagnosticChantier.$inject = ['$resource', 'DateUtils'];

    function DiagnosticChantier ($resource, DateUtils) {
        var resourceUrl =  'api/diagnostic-chantiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDiagnostic = DateUtils.convertLocalDateFromServer(data.dateDiagnostic);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateDiagnostic = DateUtils.convertLocalDateToServer(copy.dateDiagnostic);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateDiagnostic = DateUtils.convertLocalDateToServer(copy.dateDiagnostic);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
