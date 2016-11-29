(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ChantierDetailController', ChantierDetailController);

    ChantierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Chantier', 'ReceptionChantier', 'AvancementChantier', 'ReserveChantier', 'Client', 'DiagnosticChantier', 'Utilisateur', 'Commande'];

    function ChantierDetailController($scope, $rootScope, $stateParams, previousState, entity, Chantier, ReceptionChantier, AvancementChantier, ReserveChantier, Client, DiagnosticChantier, Utilisateur, Commande) {
        var vm = this;

        vm.chantier = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:chantierUpdate', function(event, result) {
            vm.chantier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
