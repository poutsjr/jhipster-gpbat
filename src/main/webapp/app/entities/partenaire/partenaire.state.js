(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('partenaire', {
            parent: 'entity',
            url: '/partenaire?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.partenaire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/partenaire/partenaires.html',
                    controller: 'PartenaireController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('partenaire');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('partenaire-detail', {
            parent: 'entity',
            url: '/partenaire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.partenaire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/partenaire/partenaire-detail.html',
                    controller: 'PartenaireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('partenaire');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Partenaire', function($stateParams, Partenaire) {
                    return Partenaire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'partenaire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('partenaire-detail.edit', {
            parent: 'partenaire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/partenaire/partenaire-dialog.html',
                    controller: 'PartenaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Partenaire', function(Partenaire) {
                            return Partenaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('partenaire.new', {
            parent: 'partenaire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/partenaire/partenaire-dialog.html',
                    controller: 'PartenaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                raisonSociale: null,
                                responsable: null,
                                contact: null,
                                adresse: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('partenaire', null, { reload: 'partenaire' });
                }, function() {
                    $state.go('partenaire');
                });
            }]
        })
        .state('partenaire.edit', {
            parent: 'partenaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/partenaire/partenaire-dialog.html',
                    controller: 'PartenaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Partenaire', function(Partenaire) {
                            return Partenaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('partenaire', null, { reload: 'partenaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('partenaire.delete', {
            parent: 'partenaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/partenaire/partenaire-delete-dialog.html',
                    controller: 'PartenaireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Partenaire', function(Partenaire) {
                            return Partenaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('partenaire', null, { reload: 'partenaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
