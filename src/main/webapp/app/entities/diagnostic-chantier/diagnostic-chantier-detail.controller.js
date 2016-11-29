(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('DiagnosticChantierDetailController', DiagnosticChantierDetailController);

    DiagnosticChantierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DiagnosticChantier', 'Chantier'];

    function DiagnosticChantierDetailController($scope, $rootScope, $stateParams, previousState, entity, DiagnosticChantier, Chantier) {
        var vm = this;

        vm.diagnosticChantier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:diagnosticChantierUpdate', function(event, result) {
            vm.diagnosticChantier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
