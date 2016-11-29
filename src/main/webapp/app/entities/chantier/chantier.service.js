(function() {
    'use strict';
    angular
        .module('gpbatApp')
        .factory('Chantier', Chantier);

    Chantier.$inject = ['$resource', 'DateUtils'];

    function Chantier ($resource, DateUtils) {
        var resourceUrl =  'api/chantiers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDebut = DateUtils.convertLocalDateFromServer(data.dateDebut);
                        data.dateDemandeTravaux = DateUtils.convertLocalDateFromServer(data.dateDemandeTravaux);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateDebut = DateUtils.convertLocalDateToServer(copy.dateDebut);
                    copy.dateDemandeTravaux = DateUtils.convertLocalDateToServer(copy.dateDemandeTravaux);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateDebut = DateUtils.convertLocalDateToServer(copy.dateDebut);
                    copy.dateDemandeTravaux = DateUtils.convertLocalDateToServer(copy.dateDemandeTravaux);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
