(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('CommandeDialogController', CommandeDialogController);

    CommandeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Commande', 'Facture', 'DetailCommande', 'Chantier', 'Partenaire', 'TypeCommande'];

    function CommandeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Commande, Facture, DetailCommande, Chantier, Partenaire, TypeCommande) {
        var vm = this;

        vm.commande = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.factures = Facture.query({filter: 'commande-is-null'});
        $q.all([vm.commande.$promise, vm.factures.$promise]).then(function() {
            if (!vm.commande.factureId) {
                return $q.reject();
            }
            return Facture.get({id : vm.commande.factureId}).$promise;
        }).then(function(facture) {
            vm.factures.push(facture);
        });
        vm.detailcommandes = DetailCommande.query();
        vm.chantiers = Chantier.query();
        vm.commandes = Commande.query();
        vm.partenaires = Partenaire.query();
        vm.typecommandes = TypeCommande.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.commande.id !== null) {
                Commande.update(vm.commande, onSaveSuccess, onSaveError);
            } else {
                Commande.save(vm.commande, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:commandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateEdition = false;
        vm.datePickerOpenStatus.dateReception = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
