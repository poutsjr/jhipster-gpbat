(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('ChantierDialogController', ChantierDialogController);

    ChantierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Chantier', 'ReceptionChantier', 'AvancementChantier', 'ReserveChantier', 'Client', 'DiagnosticChantier', 'Utilisateur', 'Commande'];

    function ChantierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Chantier, ReceptionChantier, AvancementChantier, ReserveChantier, Client, DiagnosticChantier, Utilisateur, Commande) {
        var vm = this;

        vm.chantier = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.receptions = ReceptionChantier.query({filter: 'chantier-is-null'});
        $q.all([vm.chantier.$promise, vm.receptions.$promise]).then(function() {
            if (!vm.chantier.receptionId) {
                return $q.reject();
            }
            return ReceptionChantier.get({id : vm.chantier.receptionId}).$promise;
        }).then(function(reception) {
            vm.receptions.push(reception);
        });
        vm.avancementchantiers = AvancementChantier.query();
        vm.reservechantiers = ReserveChantier.query();
        vm.clients = Client.query();
        vm.diagnosticchantiers = DiagnosticChantier.query();
        vm.utilisateurs = Utilisateur.query();
        vm.commandes = Commande.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.chantier.id !== null) {
                Chantier.update(vm.chantier, onSaveSuccess, onSaveError);
            } else {
                Chantier.save(vm.chantier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:chantierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateDebut = false;
        vm.datePickerOpenStatus.dateDemandeTravaux = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
