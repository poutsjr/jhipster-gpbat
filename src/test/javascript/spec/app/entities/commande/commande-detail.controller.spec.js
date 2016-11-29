'use strict';

describe('Controller Tests', function() {

    describe('Commande Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCommande, MockFacture, MockDetailCommande, MockChantier, MockPartenaire, MockTypeCommande;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCommande = jasmine.createSpy('MockCommande');
            MockFacture = jasmine.createSpy('MockFacture');
            MockDetailCommande = jasmine.createSpy('MockDetailCommande');
            MockChantier = jasmine.createSpy('MockChantier');
            MockPartenaire = jasmine.createSpy('MockPartenaire');
            MockTypeCommande = jasmine.createSpy('MockTypeCommande');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Commande': MockCommande,
                'Facture': MockFacture,
                'DetailCommande': MockDetailCommande,
                'Chantier': MockChantier,
                'Partenaire': MockPartenaire,
                'TypeCommande': MockTypeCommande
            };
            createController = function() {
                $injector.get('$controller')("CommandeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gpbatApp:commandeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
